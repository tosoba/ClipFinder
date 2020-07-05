package com.example.there.domain.entity.soundcloud

data class SoundCloudPlaylistEntity(
    val artworkUrl: String?,
    val createdAt: String,
    val displayDate: String,
    val duration: Int,
    val id: Int,
    val isAlbum: Boolean,
    val kind: String,
    val likesCount: Int,
    val permalink: String,
    val permalinkUrl: String,
    val `public`: Boolean,
    val publishedAt: String?,
    val repostsCount: Int,
    val title: String,
    val trackCount: Int,
    val uri: String,
    val user: SoundCloudUserEntity,
    val userId: Int
)
