package com.example.spotifyapi.model

import com.example.core.model.StringUrlModel
import com.google.gson.annotations.SerializedName

class ArtistApiModel(
        val id: String,

        @SerializedName("images")
        val icons: List<StringUrlModel>,

        val name: String,

        val popularity: Int
)

class SimplifiedArtistApiModel(
        val id: String,
        val name: String
)