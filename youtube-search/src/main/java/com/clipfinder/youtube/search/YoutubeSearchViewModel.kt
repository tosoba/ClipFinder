package com.clipfinder.youtube.search

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.youtube.ext.highestResUrl
import com.clipfinder.core.youtube.ext.isValid
import com.clipfinder.core.youtube.usecase.SearchVideos
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.model.Loading
import com.example.core.android.model.PageTokenDataList
import com.example.core.android.model.retryLoadItemsOnNetworkAvailable
import com.example.core.android.model.videos.Video
import com.example.core.ext.castAs
import com.example.core.model.Resource
import com.example.core.ext.mapData
import com.google.api.services.youtube.model.SearchResult
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.BackpressureStrategy
import org.koin.android.ext.android.get
import timber.log.Timber
import java.math.BigInteger

private typealias State = YoutubeSearchState

class YoutubeSearchViewModel(
    initialState: State,
    private val searchVideos: SearchVideos,
    context: Context
) : MvRxViewModel<State>(initialState) {

    private val clear: PublishRelay<Unit> = PublishRelay.create()

    init {
        search()
        handleConnectivityChanges(context)
    }

    fun search() = searchVideos(shouldClear = false)

    fun search(newQuery: String) = withState { (query) ->
        if (newQuery == query) return@withState
        setState { copy(query = newQuery) }
        searchVideos(shouldClear = true)
    }

    private fun searchVideos(shouldClear: Boolean = false) {
        if (shouldClear) clear.accept(Unit)
        withState { (query, videos) ->
            if (!shouldClear && !videos.shouldLoadMore) return@withState

            setState {
                if (shouldClear) copy(videos = PageTokenDataList(status = Loading))
                else copy(videos = videos.copyWithLoadingInProgress)
            }

            searchVideos.with(query, videos.nextPageToken)
                .takeUntil(clear.toFlowable(BackpressureStrategy.LATEST))
                .subscribe({
                    setState {
                        when (it) {
                            is Resource.Success -> {
                                val (newVideos, nextPageToken) = it.data
                                copy(videos = videos.copyWithNewItems(newVideos, nextPageToken))
                            }
                            is Resource.Error -> {
                                it.error?.castAs<Throwable>()?.let(::onError)
                                    ?: Timber.wtf("Unknown error")
                                copy(videos = videos.copyWithError(it))
                            }
                        }
                    }
                }, {
                    setState { copy(videos = videos.copyWithError(it)) }
                    onError(it)
                })
                .disposeOnClear()
        }
    }

    private fun onError(error: Throwable) = Timber.e(error, javaClass.simpleName.toString())

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context.handleConnectivityChanges { (_, videos) ->
            if (videos.retryLoadItemsOnNetworkAvailable) searchVideos()
        }
    }

    companion object : MvRxViewModelFactory<YoutubeSearchViewModel, State> {
        override fun create(
            viewModelContext: ViewModelContext, state: State
        ): YoutubeSearchViewModel = YoutubeSearchViewModel(
            state,
            viewModelContext.activity.get(),
            viewModelContext.app()
        )
    }
}

private fun SearchVideos.with(query: String, pageToken: String?) =
    this(applySchedulers = false, args = SearchVideos.Args(query, pageToken))
        .mapData { response ->
            Pair(
                response.items
                    ?.filter(SearchResult::isValid)
                    ?.map { result ->
                        Video(
                            id = result.id.videoId,
                            title = result.snippet.title,
                            description = result.snippet.description,
                            publishedAt = result.snippet.publishedAt,
                            thumbnailUrl = result.snippet.thumbnails.highestResUrl,
                            duration = "",
                            viewCount = BigInteger.ZERO
                        )
                    }
                    ?: emptyList(),
                response.nextPageToken
            )
        }
