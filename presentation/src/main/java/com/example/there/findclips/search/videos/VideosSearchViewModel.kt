package com.example.there.findclips.search.videos

import com.example.there.domain.usecases.videos.GetChannelsThumbnailUrlsUseCase
import com.example.there.domain.usecases.videos.SearchVideosUseCase
import com.example.there.findclips.base.BaseVideosViewModel
import com.example.there.findclips.mappers.VideoEntityMapper

class VideosSearchViewModel(private val searchVideosUseCase: SearchVideosUseCase,
                            getChannelsThumbnailUrlsUseCase: GetChannelsThumbnailUrlsUseCase) : BaseVideosViewModel(getChannelsThumbnailUrlsUseCase) {

    val viewState: VideosSearchViewState = VideosSearchViewState()

    private var lastSearchVideosNextPageToken: String? = null
    private var lastQuery: String? = null

    fun searchVideos(query: String) {
        viewState.videosLoadingInProgress.set(true)
        lastQuery = query
        addSearchVideosDisposable(query, null)
    }

    fun searchVideosWithLastQuery() {
        if (lastQuery != null && lastSearchVideosNextPageToken != null) {
            viewState.videosLoadingInProgress.set(true)
            addSearchVideosDisposable(lastQuery!!, lastSearchVideosNextPageToken)
        }
    }

    private fun addSearchVideosDisposable(query: String, pageToken: String?) {
        addDisposable(searchVideosUseCase.execute(query, pageToken)
                .doFinally { viewState.videosLoadingInProgress.set(false) }
                .subscribe({
                    val (newNextPageToken, videos) = it
                    lastSearchVideosNextPageToken = newNextPageToken
                    viewState.videos.addAll(videos.map(VideoEntityMapper::mapFrom))
                    getChannelThumbnails(videos, onSuccess = {
                        it.forEachIndexed { index, url -> viewState.videos.getOrNull(index)?.channelThumbnailUrl?.set(url) }
                    })
                }, this::onError))
    }
}