package com.example.there.domain.entity.soundcloud

data class SoundCloudDiscoverEntity(
    val playlists: List<SoundCloudPlaylistEntity>,
    val systemPlaylists: List<SoundCloudSystemPlaylistEntity>
)
