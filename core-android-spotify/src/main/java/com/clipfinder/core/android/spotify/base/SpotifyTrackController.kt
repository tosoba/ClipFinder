package com.clipfinder.core.android.spotify.base

import com.clipfinder.core.android.spotify.model.Track

interface SpotifyTrackController {
    fun updateTrack(track: Track)
}