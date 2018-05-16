package com.example.there.findclips.model.entities

import android.annotation.SuppressLint
import io.mironov.smuggler.AutoParcelable

@SuppressLint("ParcelCreator")
data class Album(
        val id: String,
        val name: String,
        val artists: List<SimpleArtist>,
        val albumType: String,
        val iconUrl: String
): AutoParcelable