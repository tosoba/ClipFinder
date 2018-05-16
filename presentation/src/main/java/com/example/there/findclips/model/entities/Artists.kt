package com.example.there.findclips.model.entities

import android.annotation.SuppressLint
import io.mironov.smuggler.AutoParcelable

@SuppressLint("ParcelCreator")
data class Artist(
        val id: String,
        val name: String,
        val popularity: Int,
        val iconUrl: String
): AutoParcelable

@SuppressLint("ParcelCreator")
data class SimpleArtist(
        val id: String,
        val name: String
) : AutoParcelable