package com.example.there.findclips.videos.player

import javax.inject.Inject


class YoutubePlayerViewModel @Inject constructor() : com.example.coreandroid.base.vm.BaseViewModel() {

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