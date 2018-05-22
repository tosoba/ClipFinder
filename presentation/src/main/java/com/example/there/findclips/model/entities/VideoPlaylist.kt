package com.example.there.findclips.model.entities

data class VideoPlaylist(val id: Long = 0, val name: String)

data class PlaylistWithVideosEntity(
        val playlistEntity: VideoPlaylist,
        val videos: List<Video>
)