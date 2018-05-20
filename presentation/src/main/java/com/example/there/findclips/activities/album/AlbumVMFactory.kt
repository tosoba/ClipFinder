package com.example.there.findclips.activities.album

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecases.spotify.GetAccessToken
import com.example.there.domain.usecases.spotify.GetArtists
import com.example.there.domain.usecases.spotify.GetTracksFromAlbum
import com.example.there.domain.usecases.spotify.InsertAlbum

@Suppress("UNCHECKED_CAST")
class AlbumVMFactory(private val getAccessToken: GetAccessToken,
                     private val getArtists: GetArtists,
                     private val getTracksFromAlbum: GetTracksFromAlbum,
                     private val insertAlbum: InsertAlbum) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            AlbumViewModel(getAccessToken, getArtists, getTracksFromAlbum, insertAlbum) as T
}