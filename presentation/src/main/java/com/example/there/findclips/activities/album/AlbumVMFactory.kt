package com.example.there.findclips.activities.album

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecases.spotify.GetAccessToken
import com.example.there.domain.usecases.spotify.GetArtists
import com.example.there.domain.usecases.spotify.GetTracksFromAlbum

@Suppress("UNCHECKED_CAST")
class AlbumVMFactory(private val getAccessToken: GetAccessToken,
                     private val getArtists: GetArtists,
                     private val getTracksFromAlbum: GetTracksFromAlbum) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            AlbumViewModel(getAccessToken, getArtists, getTracksFromAlbum) as T
}