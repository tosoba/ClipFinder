package com.example.spotifyplayer

import com.spotify.sdk.android.player.Metadata

val Metadata.Track.id: String?
    get() = if (uri != null) uri.substring(uri.lastIndexOf(':') + 1) else null
