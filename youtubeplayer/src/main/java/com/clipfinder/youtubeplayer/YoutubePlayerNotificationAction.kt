package com.clipfinder.youtubeplayer

import android.content.Intent
import android.content.IntentFilter

enum class YoutubePlayerNotificationAction {
    YOUTUBE_ACTION_PAUSE_PLAYBACK,
    YOUTUBE_ACTION_RESUME_PLAYBACK,
    YOUTUBE_ACTION_PREV_VIDEO,
    YOUTUBE_ACTION_NEXT_VIDEO;

    val filter: IntentFilter
        get() = IntentFilter(name)

    val intent: Intent
        get() = Intent(name)
}
