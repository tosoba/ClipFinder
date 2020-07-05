package com.example.there.domain.entity.videos

data class VideoPlaylistThumbnailsEntity(
    val playlist: VideoPlaylistEntity,
    val thumbnailUrls: List<String>
)