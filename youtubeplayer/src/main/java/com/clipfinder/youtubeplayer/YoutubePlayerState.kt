package com.clipfinder.youtubeplayer

import com.airbnb.mvrx.MvRxState
import com.clipfinder.core.android.model.videos.Video
import com.clipfinder.core.android.model.videos.VideoPlaylist

data class YoutubePlayerState(
    val lastPlayedVideo: Video? = null,
    val lastPlayedVideoPlaylist: VideoPlaylist? = null,
    val playbackInProgress: Boolean = false,
    val playlistVideos: List<Video> = emptyList(),
    val currentPlaylistVideoIndex: Int = 0
) : MvRxState
