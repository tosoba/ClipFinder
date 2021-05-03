package com.clipfinder.core.android.spotify.fragment

import com.clipfinder.core.android.base.fragment.IPlayerFragment
import com.clipfinder.core.android.spotify.model.Album
import com.clipfinder.core.android.spotify.model.Playlist
import com.clipfinder.core.android.spotify.model.Track

interface ISpotifyPlayerFragment : IPlayerFragment {
    val isPlayerLoggedIn: Boolean
    fun initializePlayer(accessToken: String, callback: () -> Unit)
    fun logOutPlayer()

    fun loadTrack(track: Track)
    fun loadAlbum(album: Album)
    fun loadPlaylist(playlist: Playlist)
}
