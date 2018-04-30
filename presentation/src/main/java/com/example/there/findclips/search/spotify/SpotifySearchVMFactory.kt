package com.example.there.findclips.search.spotify

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecase.spotify.AccessTokenUseCase

class SpotifySearchVMFactory(private val accessTokenUseCase: AccessTokenUseCase) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SpotifySearchViewModel(accessTokenUseCase) as T
    }
}