package com.example.youtubeplayer

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.videos.Video
import com.example.core.android.model.videos.VideoPlaylist

data class YoutubePlayerState(
    val lastPlayedVideo: Video? = null,
    val lastPlayedVideoPlaylist: VideoPlaylist? = null,
    val playbackInProgress: Boolean = false,
    val playlistVideos: List<Video> = emptyList(),
    val currentPlaylistVideoIndex: Int = 0
) : MvRxState
