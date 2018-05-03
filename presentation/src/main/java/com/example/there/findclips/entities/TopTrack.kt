package com.example.there.findclips.entities

import android.annotation.SuppressLint
import io.mironov.smuggler.AutoParcelable

@SuppressLint("ParcelCreator")
data class TopTrack(
        val position: Int,
        val track: Track
) : AutoParcelable