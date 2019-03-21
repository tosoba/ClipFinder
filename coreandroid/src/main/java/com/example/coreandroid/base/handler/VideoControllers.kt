package com.example.coreandroid.base.handler

import com.example.coreandroid.model.videos.Video
import com.example.coreandroid.model.videos.VideoPlaylist

interface VideoPlaylistController {
    fun addVideoToPlaylist(playlist: VideoPlaylist)
    fun showNewPlaylistDialog()
}

interface YoutubePlayerController {
    fun loadVideo(video: Video)
    fun loadVideoPlaylist(videoPlaylist: VideoPlaylist, videos: List<Video>)
}