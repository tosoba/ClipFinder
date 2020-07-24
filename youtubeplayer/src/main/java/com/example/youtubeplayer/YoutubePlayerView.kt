package com.example.youtubeplayer

import android.view.View
import com.example.core.android.model.videos.Video
import com.example.core.android.model.videos.VideoPlaylist

class YoutubePlayerView(
    val onYoutubePlayerCloseBtnClickListener: View.OnClickListener,
    val onYoutubePlayerPlayPauseBtnClickListener: View.OnClickListener
)

class YoutubePlayerState(
    var lastPlayedVideo: Video? = null,
    var youtubePlaybackInProgress: Boolean = false,
    var lastVideoPlaylist: VideoPlaylist? = null,
    var videosToPlay: List<Video>? = null,
    var currentVideoIndex: Int = 0
)
