package com.clipfinder.core.android.base.handler

import com.clipfinder.core.android.model.videos.Video
import com.clipfinder.core.android.model.videos.VideoPlaylist

interface YoutubePlayerController {
    fun loadVideo(video: Video)
    fun loadVideoPlaylist(videoPlaylist: VideoPlaylist, videos: List<Video>)
}
