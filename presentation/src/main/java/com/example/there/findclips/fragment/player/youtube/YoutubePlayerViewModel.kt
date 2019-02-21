package com.example.there.findclips.fragment.player.youtube

import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.model.entity.Video
import com.example.there.findclips.model.entity.VideoPlaylist
import javax.inject.Inject


class YoutubePlayerViewModel @Inject constructor() : BaseViewModel() {

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