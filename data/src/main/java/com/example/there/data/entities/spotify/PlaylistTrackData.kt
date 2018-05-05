package com.example.there.data.entities.spotify

import com.google.gson.annotations.SerializedName

data class PlaylistTrackData(
        @SerializedName("added_at") val addedAt: String,
        val track: TrackData
)