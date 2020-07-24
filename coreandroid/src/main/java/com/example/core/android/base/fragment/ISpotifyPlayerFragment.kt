package com.example.core.android.base.fragment

import com.example.core.android.model.spotify.Album
import com.example.core.android.model.spotify.Playlist
import com.example.core.android.model.spotify.Track

interface ISpotifyPlayerFragment: IPlayerFragment {
    val isPlayerLoggedIn: Boolean
    val isPlayerInitialized: Boolean

    fun onAuthenticationComplete(accessToken: String)
    fun logOutPlayer()

    val lastPlayedTrack: Track?
    val lastPlayedAlbum: Album?
    val lastPlayedPlaylist: Playlist?

    fun loadTrack(track: Track)
    fun loadAlbum(album: Album)
    fun loadPlaylist(playlist: Playlist)
}
