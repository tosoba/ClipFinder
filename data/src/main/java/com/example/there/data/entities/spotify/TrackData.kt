package com.example.there.data.entities.spotify

import com.google.gson.annotations.SerializedName

data class TrackData(
        val id: String,
        val name: String,
        val artists: List<SimplifiedArtistData>,
        val album: SimplifiedAlbumData,
        @SerializedName("duration_ms") val durationMs: Int,
        val popularity: Int,
        @SerializedName("track_number") val trackNumber: Int
)

data class SimilarTrackData(
        val id: String,
        val name: String,
        val artists: List<SimplifiedArtistData>
)

data class PlaylistTrackData(
        @SerializedName("added_at") val addedAt: String,
        val track: TrackData
)