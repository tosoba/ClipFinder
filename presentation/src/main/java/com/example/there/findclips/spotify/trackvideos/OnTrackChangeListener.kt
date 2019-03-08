package com.example.there.findclips.spotify.trackvideos

import com.example.there.findclips.model.entity.spotify.Track

interface OnTrackChangeListener {
    fun onTrackChanged(newTrack: Track)
}