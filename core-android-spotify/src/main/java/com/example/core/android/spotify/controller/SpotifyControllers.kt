package com.example.core.android.spotify.controller

import androidx.lifecycle.LiveData
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Track
import com.example.core.android.spotify.model.Playlist

interface SpotifyPlayerController {
    val isPlayerLoggedIn: Boolean
    fun loadTrack(track: Track)
    fun loadAlbum(album: Album)
    fun loadPlaylist(playlist: Playlist)
}

interface SpotifyTrackChangeHandler {
    fun onTrackChanged(trackId: String)
}

interface SpotifyAuthController {
    val isLoggedIn: LiveData<Boolean>
    var onLoginSuccessful: (() -> Unit)?
    fun showLoginDialog()
    fun logOut()
}
