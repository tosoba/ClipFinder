package com.example.there.data.entities.spotify

import com.google.gson.annotations.SerializedName

data class TrackData(
        val id: String,
        val name: String,
        val href: String,
        val artists: List<TrackArtist>,
        val album: TrackAlbum,
        @SerializedName("duration_ms") val durationMs: Int,
        val popularity: Int,
        @SerializedName("track_number") val trackNumber: Int
)

data class TrackAlbum(
        val id: String,
        val name: String,
        @SerializedName("images") val icons: List<IconData>
)

data class TrackArtist(
        val id: String,
        val name: String
)