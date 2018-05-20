package com.example.there.findclips.activities.artist

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecases.spotify.*

@Suppress("UNCHECKED_CAST")
class ArtistVMFactory(private val getAccessToken: GetAccessToken,
                      private val getAlbumsFromArtist: GetAlbumsFromArtist,
                      private val getTopTracksFromArtist: GetTopTracksFromArtist,
                      private val getRelatedArtists: GetRelatedArtists,
                      private val insertArtist: InsertArtist) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            ArtistViewModel(getAccessToken, getAlbumsFromArtist, getTopTracksFromArtist, getRelatedArtists, insertArtist) as T
}