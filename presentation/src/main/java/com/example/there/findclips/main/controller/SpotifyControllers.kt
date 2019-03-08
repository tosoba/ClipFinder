package com.example.there.findclips.main.controller

import android.databinding.ObservableField
import com.example.there.findclips.model.entity.spotify.Album
import com.example.there.findclips.model.entity.spotify.Playlist
import com.example.there.findclips.model.entity.spotify.Track

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