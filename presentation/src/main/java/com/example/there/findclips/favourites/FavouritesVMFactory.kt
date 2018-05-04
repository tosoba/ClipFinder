package com.example.there.findclips.favourites

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecases.spotify.AccessTokenUseCase

class FavouritesVMFactory(private val accessTokenUseCase: AccessTokenUseCase) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavouritesViewModel(accessTokenUseCase) as T
    }
}