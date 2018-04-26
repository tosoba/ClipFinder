package com.example.there.findclips.videos

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecase.spotify.AccessTokenUseCase
import com.example.there.domain.usecase.videos.SearchVideosUseCase

class VideosViewModelFactory(private val accessTokenUseCase: AccessTokenUseCase,
                             private val searchVideosUseCase: SearchVideosUseCase) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VideosViewModel(accessTokenUseCase, searchVideosUseCase) as T
    }
}