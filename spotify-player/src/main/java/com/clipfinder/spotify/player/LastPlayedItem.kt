package com.clipfinder.spotify.player

import com.clipfinder.core.android.spotify.model.Album
import com.clipfinder.core.android.spotify.model.Playlist
import com.clipfinder.core.android.spotify.model.Track

sealed class LastPlayedItem
object NoLastPlayedItem : LastPlayedItem()
data class LastPlayedTrack(val track: Track) : LastPlayedItem()
data class LastPlayedAlbum(val album: Album) : LastPlayedItem()
data class LastPlayedPlaylist(val playlist: Playlist) : LastPlayedItem()