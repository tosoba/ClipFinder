package com.example.main.controller

interface VideoPlaylistController {
    fun addVideoToPlaylist(playlist: VideoPlaylist)
    fun showNewPlaylistDialog()
}

interface YoutubePlayerController {
    fun loadVideo(video: Video)
    fun loadVideoPlaylist(videoPlaylist: VideoPlaylist, videos: List<Video>)
}