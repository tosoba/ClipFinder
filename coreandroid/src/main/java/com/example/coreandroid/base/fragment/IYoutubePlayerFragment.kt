package com.example.coreandroid.base.fragment

import com.example.coreandroid.model.videos.Video
import com.example.coreandroid.model.videos.VideoPlaylist

interface IYoutubePlayerFragment: IPlayerFragment {
    fun loadVideo(video: Video)
    fun loadVideoPlaylist(videoPlaylist: VideoPlaylist, videos: List<Video>)

    val lastPlayedVideo: Video?
}