package com.clipfinder.core.soundcloud.model

interface ISoundCloudPlaylist {
    val artworkUrl: String?
    val createdAt: String
    val duration: Int
    val id: Int
    val likesCount: Int
    val publishedAt: String?
    val title: String
    val trackCount: Int
    val userId: Int
}
