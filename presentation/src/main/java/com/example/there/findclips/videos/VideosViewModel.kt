package com.example.there.findclips.videos

import com.example.there.domain.usecase.spotify.AccessTokenUseCase
import com.example.there.domain.usecase.videos.SearchVideosUseCase
import com.example.there.findclips.base.BaseViewModel

class VideosViewModel(accessTokenUseCase: AccessTokenUseCase,
                      private val searchVideosUseCase: SearchVideosUseCase) : BaseViewModel(accessTokenUseCase) {

    val viewState: VideosViewState = VideosViewState()

    fun getVideos(query: String) {
        viewState.videosLoadingInProgress.set(true)
        addDisposable(searchVideosUseCase.getVideos(query)
                .doFinally { viewState.videosLoadingInProgress.set(false) }
                .subscribe({ viewState.videos.addAll(it) }, this::onError))
    }
}