package com.example.there.findclips.player

import android.databinding.ObservableField

open class PlayerViewState(
        val videoIsOpen: ObservableField<Boolean> = ObservableField(false),
        val isMaximized: ObservableField<Boolean> = ObservableField(true)
)