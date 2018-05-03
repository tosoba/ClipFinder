package com.example.there.findclips.entities

import android.annotation.SuppressLint
import io.mironov.smuggler.AutoParcelable

@SuppressLint("ParcelCreator")
data class Track(
        val id: String,
        val name: String,
        val iconUrl: String,
        val albumId: String,
        val albumName: String,
        val artists: List<SimpleArtist>
) : AutoParcelable {

    val artistNamesText: String
        get() = artists.joinToString(separator = ", ") { it.name }

    val query: String
        get() = "$name $albumName"
}