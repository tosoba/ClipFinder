package com.example.main.controller

import android.databinding.ObservableField

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