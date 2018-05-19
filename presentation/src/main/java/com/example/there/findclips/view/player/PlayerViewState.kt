package com.example.there.findclips.view.player

import android.databinding.ObservableField

data class PlayerViewState(
        val videoIsOpen: ObservableField<Boolean> = ObservableField(false),
        val isMaximized: ObservableField<Boolean> = ObservableField(true)
)