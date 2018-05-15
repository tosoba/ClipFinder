package com.example.there.findclips.trackvideos

import com.example.there.findclips.entities.Track

interface OnTrackChangeListener {
    fun onTrackChanged(newTrack: Track)
}