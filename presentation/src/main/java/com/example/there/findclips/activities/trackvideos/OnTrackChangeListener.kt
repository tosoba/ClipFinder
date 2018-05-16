package com.example.there.findclips.activities.trackvideos

import com.example.there.findclips.model.entities.Track

interface OnTrackChangeListener {
    fun onTrackChanged(newTrack: Track)
}