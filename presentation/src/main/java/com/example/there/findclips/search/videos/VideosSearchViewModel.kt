package com.example.there.findclips.search.videos

import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.usecases.videos.GetChannelsThumbnailUrlsUseCase
import com.example.there.domain.usecases.videos.SearchVideosUseCase
import com.example.there.findclips.base.BaseViewModel
import com.example.there.findclips.mappers.VideoEntityMapper

class VideosSearchViewModel(private val searchVideosUseCase: SearchVideosUseCase,
                            private val getChannelsThumbnailUrlsUseCase: GetChannelsThumbnailUrlsUseCase) : BaseViewModel() {

    val viewState: VideosSearchViewState = VideosSearchViewState()

    fun getVideos(query: String) {
        viewState.videosLoadingInProgress.set(true)

        addDisposable(searchVideosUseCase.getVideos(query)
                .doFinally { viewState.videosLoadingInProgress.set(false) }
                .subscribe({
                    viewState.videos.addAll(it.map(VideoEntityMapper::mapFrom))
                    getChannelThumbnails(it)
                }, this::onError))
    }

    private fun getChannelThumbnails(videos: List<VideoEntity>) {
        addDisposable(getChannelsThumbnailUrlsUseCase.getUrls(videos)
                .subscribe({
                    it.forEachIndexed { index, url -> viewState.videos.getOrNull(index)?.channelThumbnailUrl?.set(url) }
                }, this::onError))
    }
}