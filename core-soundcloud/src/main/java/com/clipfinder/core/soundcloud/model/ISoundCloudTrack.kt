package com.clipfinder.core.soundcloud.model

interface ISoundCloudTrack {
    val id: Int
    val title: String
    val artworkUrl: String?
    val description: String
    val duration: Int
    val genre: String
    val tags: String
    val streamUrl: String?
    val waveformUrl: String
}
