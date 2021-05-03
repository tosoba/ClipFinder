package com.clipfinder.spotify.player

import android.content.Intent
import android.content.IntentFilter

enum class SpotifyPlayerNotificationAction {
    SPOTIFY_ACTION_PAUSE_PLAYBACK,
    SPOTIFY_ACTION_RESUME_PLAYBACK,
    SPOTIFY_ACTION_PREV_TRACK,
    SPOTIFY_ACTION_NEXT_TRACK;

    val filter: IntentFilter
        get() = IntentFilter(name)

    val intent: Intent
        get() = Intent(name)
}
