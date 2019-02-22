package com.example.there.domain.entity.soundcloud

data class SoundCloudSystemPlaylistEntity(
        val artworkUrl: String?,
        val calculatedArtworkUrl: String,
        val description: String,
        val id: String,
        val kind: String,
        val lastUpdated: String,
        val permalink: String,
        val shortDescription: String,
        val shortTitle: String,
        val title: String,
        val trackingFeatureName: String,
        val tracks: List<SoundCloudTrackEntity>
)