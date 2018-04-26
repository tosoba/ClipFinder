package com.example.there.findclips.videos

import com.example.there.domain.usecase.spotify.AccessTokenUseCase
import com.example.there.domain.usecase.videos.SearchVideosUseCase
import com.example.there.findclips.base.BaseViewModel

class VideosViewModel(accessTokenUseCase: AccessTokenUseCase,
                      private val searchVideosUseCase: SearchVideosUseCase) : BaseViewModel(accessTokenUseCase) {

    fun getVideos(query: String) {
        addDisposable(searchVideosUseCase.getVideos(query)
                .subscribe({
                }, {
                }))
    }
}