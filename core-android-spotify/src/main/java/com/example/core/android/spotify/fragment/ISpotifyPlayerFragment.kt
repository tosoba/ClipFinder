package com.example.core.android.spotify.fragment

import com.example.core.android.base.fragment.IPlayerFragment
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Playlist
import com.example.core.android.spotify.model.Track

interface ISpotifyPlayerFragment: IPlayerFragment {
    val isPlayerLoggedIn: Boolean
    val isPlayerInitialized: Boolean

    fun onAuthenticationComplete(accessToken: String)
    fun logOutPlayer()

    fun loadTrack(track: Track)
    fun loadAlbum(album: Album)
    fun loadPlaylist(playlist: Playlist)
}
