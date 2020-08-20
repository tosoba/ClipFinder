package com.clipfinder.core.soundcloud.model

interface ISoundCloudPlaylistSelection {
    val description: String?
    val id: String
    val title: String
    val playlists: List<ISoundCloudPlaylist>
}
