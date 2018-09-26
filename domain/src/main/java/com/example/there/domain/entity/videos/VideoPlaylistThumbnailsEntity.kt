package com.example.there.domain.entity.videos

data class VideoPlaylistThumbnailsEntity(
        val playlistId: Long,
        val playlistName: String,
        val thumbnailUrls: List<String>
)