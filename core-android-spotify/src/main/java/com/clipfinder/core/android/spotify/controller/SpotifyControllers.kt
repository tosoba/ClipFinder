package com.clipfinder.core.android.spotify.controller

import com.clipfinder.core.android.spotify.model.Album
import com.clipfinder.core.android.spotify.model.Playlist
import com.clipfinder.core.android.spotify.model.Track

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
    var onLoginSuccessful: (() -> Unit)?
    fun showLoginDialog()
    fun logOutPlayer()
}
