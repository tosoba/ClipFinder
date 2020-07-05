package com.example.spotifyapi.model

import com.google.gson.annotations.SerializedName

data class TrackApiModel(
    val id: String,

    val name: String,

    val artists: List<SimplifiedArtistApiModel>,

    val album: SimplifiedAlbumApiModel,

    val popularity: Int,

    @SerializedName("track_number")
    val trackNumber: Int,

    val uri: String,

    @SerializedName("duration_ms")
    val durationMs: Int
)

data class SimilarTrackApiModel(
    val id: String,
    val name: String,
    val artists: List<SimplifiedArtistApiModel>
)

data class PlaylistTrackApiModel(
    @SerializedName("added_at")
    val addedAt: String,

    val track: TrackApiModel
)
