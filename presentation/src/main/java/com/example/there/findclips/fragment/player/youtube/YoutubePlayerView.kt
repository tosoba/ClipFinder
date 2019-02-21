package com.example.there.findclips.fragment.player.youtube

import android.view.View
import com.example.there.findclips.model.entity.Video
import com.example.there.findclips.model.entity.VideoPlaylist

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