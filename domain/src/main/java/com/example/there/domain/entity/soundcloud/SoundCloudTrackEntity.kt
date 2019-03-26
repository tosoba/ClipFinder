package com.example.there.domain.entity.soundcloud

data class SoundCloudTrackEntity(
        val id: String,
        val title: String,
        val artworkUrl: String?,
        val description: String?,
        val duration: String,
        val genre: String?,
        val tags: String?,
        val streamUrl: String?,
        val downloadUrl: String?,
        val waveformUrl: String?
)
