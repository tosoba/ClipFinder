package com.example.there.findclips.search.spotify

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecases.spotify.GetAccessToken
import com.example.there.domain.usecases.spotify.SearchSpotify

class SpotifySearchVMFactory(private val getAccessToken: GetAccessToken,
                             private val searchSpotify: SearchSpotify) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SpotifySearchViewModel(getAccessToken, searchSpotify) as T
    }
}