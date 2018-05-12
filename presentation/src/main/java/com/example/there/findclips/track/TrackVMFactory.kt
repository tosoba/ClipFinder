package com.example.there.findclips.track

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecases.spotify.GetAccessToken
import com.example.there.domain.usecases.spotify.GetAlbum
import com.example.there.domain.usecases.spotify.GetArtists
import com.example.there.domain.usecases.spotify.GetSimilarTracks

class TrackVMFactory(private val getAccessToken: GetAccessToken,
                     private val getAlbum: GetAlbum,
                     private val getArtists: GetArtists,
                     private val getSimilarTracks: GetSimilarTracks): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            TrackViewModel(getAccessToken, getAlbum, getArtists, getSimilarTracks) as T
}