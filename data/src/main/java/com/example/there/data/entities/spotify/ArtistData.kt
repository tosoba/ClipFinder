package com.example.there.data.entities.spotify

import com.google.gson.annotations.SerializedName

data class ArtistData(
        val id: String,
        @SerializedName("images") val icons: List<IconData>,
        val name: String,
        val popularity: Int
)

data class SimplifiedArtistData(
        val id: String,
        val name: String
)