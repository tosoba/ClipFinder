package com.clipfinder.core.android.spotify.ext

import com.spotify.sdk.android.player.Metadata

val Metadata.Track.id: String?
    get() = if (uri != null) uri.substring(uri.lastIndexOf(':') + 1) else null

val Metadata.Track.summaryText: String
    get() {
        return "${
            artistName ?: "Unknown artist"
        } - ${
            albumName ?: "Unknown album"
        }"
    }
