package com.clipfinder.core.android.spotify.base

import com.clipfinder.core.android.spotify.model.Album
import com.clipfinder.core.android.spotify.model.Playlist
import com.clipfinder.core.android.spotify.model.Track

interface SpotifyPlayerController {
    val isPlayerLoggedIn: Boolean
    fun loadTrack(track: Track)
    fun loadAlbum(album: Album)
    fun loadPlaylist(playlist: Playlist)
}