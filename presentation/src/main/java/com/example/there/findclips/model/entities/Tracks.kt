package com.example.there.findclips.model.entities

import android.annotation.SuppressLint
import io.mironov.smuggler.AutoParcelable

@SuppressLint("ParcelCreator")
data class Track(
        val id: String,
        val name: String,
        val iconUrl: String,
        val albumId: String,
        val albumName: String,
        val artists: List<SimpleArtist>,
        val popularity: Int,
        val trackNumber: Int
) : AutoParcelable {

    val artistNamesText: String
        get() = artists.joinToString(separator = ", ") { it.name }

    val query: String
        get() = "$name $albumName"
}

@SuppressLint("ParcelCreator")
data class TopTrack(val position: Int, val track: Track) : AutoParcelable