package com.example.coreandroid.base.handler

import android.databinding.ObservableField
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.Playlist
import com.example.coreandroid.model.spotify.Track

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