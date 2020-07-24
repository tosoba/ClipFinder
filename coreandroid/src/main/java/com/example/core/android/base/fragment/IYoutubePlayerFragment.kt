package com.example.core.android.base.fragment

import com.example.core.android.model.videos.Video
import com.example.core.android.model.videos.VideoPlaylist

interface IYoutubePlayerFragment: IPlayerFragment {
    val lastPlayedVideo: Video?

    fun loadVideo(video: Video)
    fun loadVideoPlaylist(videoPlaylist: VideoPlaylist, videos: List<Video>)
}
