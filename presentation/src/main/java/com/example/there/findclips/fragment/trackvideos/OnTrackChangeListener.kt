package com.example.there.findclips.fragment.trackvideos

import com.example.there.findclips.model.entity.Track

interface OnTrackChangeListener {
    fun onTrackChanged(newTrack: Track)
}