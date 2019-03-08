package com.example.there.findclips.main.controller

import com.example.there.findclips.model.entity.videos.Video
import com.example.there.findclips.model.entity.videos.VideoPlaylist

interface VideoPlaylistController {
    fun addVideoToPlaylist(playlist: VideoPlaylist)
    fun showNewPlaylistDialog()
}

interface YoutubePlayerController {
    fun loadVideo(video: Video)
    fun loadVideoPlaylist(videoPlaylist: VideoPlaylist, videos: List<Video>)
}