package com.example.there.findclips.videos

import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.usecase.spotify.AccessTokenUseCase
import com.example.there.domain.usecase.videos.GetChannelsThumbnailUrlsUseCase
import com.example.there.domain.usecase.videos.SearchVideosUseCase
import com.example.there.findclips.base.BaseViewModel
import com.example.there.findclips.entities.Video

class VideosViewModel(accessTokenUseCase: AccessTokenUseCase,
                      private val searchVideosUseCase: SearchVideosUseCase,
                      private val getChannelsThumbnailUrlsUseCase: GetChannelsThumbnailUrlsUseCase,
                      private val videoEntityMapper: Mapper<VideoEntity, Video>) : BaseViewModel(accessTokenUseCase) {

    val viewState: VideosViewState = VideosViewState()

    fun getVideos(query: String) {
        viewState.videosLoadingInProgress.set(true)
        addDisposable(searchVideosUseCase.getVideos(query)
                .doFinally { viewState.videosLoadingInProgress.set(false) }
                .subscribe({
                    viewState.videos.addAll(it.map(videoEntityMapper::mapFrom))
                    getChannelThumbnails(it)
                }, this::onError))
    }

    private fun getChannelThumbnails(videos: List<VideoEntity>) {
        addDisposable(getChannelsThumbnailUrlsUseCase.getUrls(videos)
                .subscribe({ it.forEachIndexed { index, url -> viewState.videos[index].channelThumbnailUrl.set(url) } }, this::onError))
    }
}