package com.example.there.findclips.search.videos

import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.usecases.videos.GetChannelsThumbnailUrlsUseCase
import com.example.there.domain.usecases.videos.SearchVideosUseCase
import com.example.there.findclips.base.BaseViewModel
import com.example.there.findclips.mappers.VideoEntityMapper

class VideosSearchViewModel(private val searchVideosUseCase: SearchVideosUseCase,
                            private val getChannelsThumbnailUrlsUseCase: GetChannelsThumbnailUrlsUseCase) : BaseViewModel() {

    val viewState: VideosSearchViewState = VideosSearchViewState()

    private var nextPageToken: String? = null

    private var lastQuery: String? = null

    fun searchVideos(query: String) {
        viewState.videosLoadingInProgress.set(true)
        lastQuery = query
        loadData(query, null)
    }

    fun searchVideosWithLastQuery() {
        if (lastQuery != null && nextPageToken != null) {
            viewState.videosLoadingInProgress.set(true)
            loadData(lastQuery!!, nextPageToken)
        }
    }

    private fun loadData(query: String, pageToken: String?) {
        addDisposable(searchVideosUseCase.execute(query, pageToken)
                .doFinally { viewState.videosLoadingInProgress.set(false) }
                .subscribe({
                    val (newNextPageToken, videos) = it
                    nextPageToken = newNextPageToken
                    viewState.videos.addAll(videos.map(VideoEntityMapper::mapFrom))
                    getChannelThumbnails(videos)
                }, this::onError))
    }

    private fun getChannelThumbnails(videos: List<VideoEntity>) {
        addDisposable(getChannelsThumbnailUrlsUseCase.execute(videos)
                .subscribe({
                    it.forEachIndexed { index, url -> viewState.videos.getOrNull(index)?.channelThumbnailUrl?.set(url) }
                }, this::onError))
    }
}