package com.example.there.findclips.activities.trackvideos

import com.example.there.domain.usecases.spotify.InsertTrack
import com.example.there.findclips.base.BaseViewModel

class TrackVideosViewModel(private val insertTrack: InsertTrack): BaseViewModel() {
    val viewState = TrackVideosViewState()
}