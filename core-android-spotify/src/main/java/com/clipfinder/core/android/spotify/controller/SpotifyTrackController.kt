package com.clipfinder.core.android.spotify.controller

import com.clipfinder.core.android.spotify.model.Track

interface SpotifyTrackController {
    fun updateTrack(track: Track)
}