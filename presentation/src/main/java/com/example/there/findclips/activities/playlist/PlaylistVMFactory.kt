package com.example.there.findclips.activities.playlist

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecases.spotify.GetAccessToken
import com.example.there.domain.usecases.spotify.GetPlaylistTracks
import com.example.there.domain.usecases.spotify.InsertSpotifyPlaylist

@Suppress("UNCHECKED_CAST")
class PlaylistVMFactory(private val getAccessToken: GetAccessToken,
                        private val getPlaylistTracks: GetPlaylistTracks,
                        private val insertSpotifyPlaylist: InsertSpotifyPlaylist) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = PlaylistViewModel(getAccessToken, getPlaylistTracks, insertSpotifyPlaylist) as T
}