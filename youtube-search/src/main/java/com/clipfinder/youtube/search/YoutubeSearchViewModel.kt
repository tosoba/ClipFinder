package com.clipfinder.youtube.search

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.ext.castAs
import com.clipfinder.core.ext.mapData
import com.clipfinder.core.model.Resource
import com.clipfinder.core.youtube.ext.highestResUrl
import com.clipfinder.core.youtube.ext.isValid
import com.clipfinder.core.youtube.usecase.SearchVideos
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.model.LoadingFirst
import com.example.core.android.model.PageTokenList
import com.example.core.android.model.Ready
import com.example.core.android.model.WithValue
import com.example.core.android.model.videos.Video
import com.example.core.android.util.ext.completed
import com.example.core.android.util.ext.retryLoadCollectionOnConnected
import com.google.api.services.youtube.model.SearchResult
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.BackpressureStrategy
import io.reactivex.Single
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

    fun clearVideosError() = clearErrorIn(State::videos) { copy(videos = it) }

    private fun searchVideos(shouldClear: Boolean = false) {
        if (shouldClear) clear.accept(Unit)
        withState { (query, videos) ->
            if (!shouldClear && videos.completed) return@withState

            setState {
                if (shouldClear) copy(videos = LoadingFirst)
                else copy(videos = videos.copyWithLoadingInProgress)
            }

            searchVideos.with(query, if (videos is WithValue) videos.value.nextPageToken else null)
                .takeUntil(clear.toFlowable(BackpressureStrategy.LATEST))
                .subscribe({
                    setState {
                        when (it) {
                            is Resource.Success -> {
                                val (newVideos, nextPageToken) = it.data
                                copy(videos = Ready(
                                    if (videos is WithValue) videos.value.copyWith(newVideos, nextPageToken)
                                    else PageTokenList(newVideos, nextPageToken)
                                ))
                            }
                            is Resource.Error -> {
                                it.error?.castAs<Throwable>()?.let(::log)
                                    ?: Timber.wtf("Unknown error")
                                copy(videos = videos.copyWithError(it))
                            }
                        }
                    }
                }, {
                    setState { copy(videos = videos.copyWithError(it)) }
                    log(it)
                })
                .disposeOnClear()
        }
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context.handleConnectivityChanges { (_, videos) ->
            if (videos.retryLoadCollectionOnConnected) searchVideos()
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

private fun SearchVideos.with(
    query: String, pageToken: String?
): Single<Resource<Pair<List<Video>, String>>> = this(applySchedulers = false, args = SearchVideos.Args(query, pageToken))
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
