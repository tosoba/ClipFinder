package com.example.youtubeplayer

import com.example.core.android.base.vm.BaseViewModel
import com.example.core.android.model.videos.Video
import com.example.core.android.model.videos.VideoPlaylist


class YoutubePlayerViewModel : BaseViewModel() {

    val playerState = YoutubePlayerState()

    fun onLoadVideo(video: Video) = with(playerState) {
        lastPlayedVideo = video
        lastVideoPlaylist = null
    }

    fun onLoadVideoPlaylist(videoPlaylist: VideoPlaylist, videos: List<Video>) = with(playerState) {
        lastVideoPlaylist = videoPlaylist
        videosToPlay = videos
        currentVideoIndex = 0
        lastPlayedVideo = null
    }
}
