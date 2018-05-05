package com.example.there.findclips.playlist

import android.databinding.ObservableField

data class PlaylistViewState(
        val loadingInProgress: ObservableField<Boolean> = ObservableField(false)
)