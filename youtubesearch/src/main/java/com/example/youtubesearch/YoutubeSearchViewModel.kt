package com.example.youtubesearch

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.youtube.ext.highUrl
import com.clipfinder.core.youtube.usecase.SearchVideos
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.model.Loading
import com.example.core.android.model.PageTokenDataList
import com.example.core.android.model.retryLoadItemsOnNetworkAvailable
import com.example.core.android.model.videos.Video
import com.example.core.ext.castAs
import com.example.core.model.Resource
import com.example.core.model.mapData
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.BackpressureStrategy
import org.koin.android.ext.android.get
import timber.log.Timber
import java.math.BigInteger

class YoutubeSearchViewModel(
    initialState: YoutubeSearchState,
    private val searchVideos: SearchVideos,
    context: Context
) : MvRxViewModel<YoutubeSearchState>(initialState) {

    init {
        handleConnectivityChanges(context)
    }

    private val clear: PublishRelay<Unit> = PublishRelay.create()

    fun search() = searchVideos(shouldClear = false)

    fun search(query: String) {
        setState { copy(query = query) }
        searchVideos(shouldClear = true)
    }

    private fun searchVideos(shouldClear: Boolean = false) {
        if (shouldClear) clear.accept(Unit)
        withState { (query, videos) ->
            if (shouldClear || videos.shouldLoadMore) return@withState

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
                            is Resource.Error<*, *> -> {
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

    companion object : MvRxViewModelFactory<YoutubeSearchViewModel, YoutubeSearchState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: YoutubeSearchState
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
                response.items?.map { result ->
                    Video(
                        id = result.id.videoId,
                        channelId = result.id.channelId,
                        title = result.snippet.title,
                        description = result.snippet.description,
                        publishedAt = result.snippet.publishedAt,
                        thumbnailUrl = result.snippet.thumbnails.highUrl,
                        duration = "",
                        viewCount = BigInteger.ZERO
                    )
                } ?: emptyList(),
                response.nextPageToken
            )
        }
