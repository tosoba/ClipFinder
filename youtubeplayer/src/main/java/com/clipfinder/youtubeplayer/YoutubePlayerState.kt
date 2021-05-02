package com.clipfinder.youtubeplayer

import com.airbnb.mvrx.MvRxState
import com.clipfinder.core.android.model.videos.Video

data class YoutubePlayerState(
    val playbackInProgress: Boolean = false,
    val mode: YoutubePlayerMode = YoutubePlayerMode.Idle,
    val showingPlaybackNotification: Boolean = false
) : MvRxState {
    val currentVideo: Video?
        get() = when (mode) {
            is YoutubePlayerMode.Playlist -> mode.videos[mode.currentVideoIndex]
            is YoutubePlayerMode.SingleVideo -> mode.video
            else -> null
        }
}

