package com.example.there.domain.entities.videos

data class VideoPlaylistEntity(
        val id: Long = 0,
        val name: String
)

data class PlaylistWithVideosEntity(
        val playlistEntity: VideoPlaylistEntity,
        val videos: List<VideoEntity>
)