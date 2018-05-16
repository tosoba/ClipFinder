package com.example.there.findclips.fragments.favourites

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecases.spotify.GetAccessToken

class FavouritesVMFactory(private val getAccessToken: GetAccessToken) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavouritesViewModel(getAccessToken) as T
    }
}