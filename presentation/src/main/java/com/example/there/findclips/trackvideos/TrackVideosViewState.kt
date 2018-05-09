package com.example.there.findclips.trackvideos

import android.databinding.ObservableField

data class TrackVideosViewState(
        val videoIsOpen: ObservableField<Boolean> = ObservableField(false)
)