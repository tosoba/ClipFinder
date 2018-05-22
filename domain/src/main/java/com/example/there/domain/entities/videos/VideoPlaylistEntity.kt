package com.example.there.domain.entities.videos

data class VideoPlaylistEntity(
        val id: Long,
        val name: String
)

data class PlaylistWithVideosEntity(
        val playlistEntity: VideoPlaylistEntity,
        val videos: List<VideoEntity>
)