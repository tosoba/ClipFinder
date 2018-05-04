package com.example.there.findclips.search.spotify

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecases.spotify.AccessTokenUseCase
import com.example.there.domain.usecases.spotify.SearchAllUseCase

class SpotifySearchVMFactory(private val accessTokenUseCase: AccessTokenUseCase,
                             private val searchAllUseCase: SearchAllUseCase) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SpotifySearchViewModel(accessTokenUseCase, searchAllUseCase) as T
    }
}