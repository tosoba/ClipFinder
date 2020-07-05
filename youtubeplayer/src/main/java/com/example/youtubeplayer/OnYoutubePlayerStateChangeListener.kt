package com.example.youtubeplayer

import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerListener

interface OnYoutubePlayerStateChangeListener : YouTubePlayerListener {
    override fun onPlaybackQualityChange(playbackQuality: PlayerConstants.PlaybackQuality) = Unit
    override fun onVideoDuration(duration: Float) = Unit
    override fun onCurrentSecond(second: Float) = Unit
    override fun onReady() = Unit
    override fun onVideoLoadedFraction(loadedFraction: Float) = Unit
    override fun onPlaybackRateChange(playbackRate: PlayerConstants.PlaybackRate) = Unit
    override fun onVideoId(videoId: String) = Unit
    override fun onApiChange() = Unit
    override fun onError(error: PlayerConstants.PlayerError) = Unit
}
