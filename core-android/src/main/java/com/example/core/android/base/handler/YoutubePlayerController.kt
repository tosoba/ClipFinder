package com.example.core.android.base.handler

import com.example.core.android.model.videos.Video
import com.example.core.android.model.videos.VideoPlaylist

interface YoutubePlayerController {
    fun loadVideo(video: Video)
    fun loadVideoPlaylist(videoPlaylist: VideoPlaylist, videos: List<Video>)
}
