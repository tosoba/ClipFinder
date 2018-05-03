package com.example.there.findclips.entities

import android.annotation.SuppressLint
import io.mironov.smuggler.AutoParcelable

@SuppressLint("ParcelCreator")
data class SimpleArtist(
        val id: String,
        val name: String
) : AutoParcelable