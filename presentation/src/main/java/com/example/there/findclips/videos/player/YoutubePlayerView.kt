package com.example.there.findclips.videos.player

import android.view.View
import com.example.there.findclips.model.entity.videos.Video
import com.example.there.findclips.model.entity.videos.VideoPlaylist

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