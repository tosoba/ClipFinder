package com.example.coreandroid.base.handler

import com.example.coreandroid.model.spotify.Track

interface OnTrackChangeListener {
    fun onTrackChanged(newTrack: Track)
}