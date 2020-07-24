package com.example.core.android.base.handler

import androidx.databinding.ObservableField
import com.example.core.android.model.spotify.Album
import com.example.core.android.model.spotify.Playlist
import com.example.core.android.model.spotify.Track

interface SpotifyPlayerController {
    val isPlayerLoggedIn: Boolean
    fun loadTrack(track: Track)
    fun loadAlbum(album: Album)
    fun loadPlaylist(playlist: Playlist)
}

interface SpotifyTrackChangeHandler {
    fun onTrackChanged(trackId: String)
}

interface SpotifyLoginController {
    val loggedInObservable: ObservableField<Boolean>
    var onLoginSuccessful: (() -> Unit)?
    fun showLoginDialog()
}
