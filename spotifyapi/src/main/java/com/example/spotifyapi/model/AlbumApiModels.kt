package com.example.spotifyapi.model

import com.example.core.model.StringUrlModel
import com.google.gson.annotations.SerializedName

class AlbumApiModel(
        @SerializedName("album_type")
        val albumType: String,

        val artists: List<SimplifiedArtistApiModel>,

        val id: String,

        @SerializedName("images")
        val icons: List<StringUrlModel>,

        val name: String,

        val uri: String
)

class SimplifiedAlbumApiModel(
        val id: String,

        val name: String,

        @SerializedName("images")
        val icons: List<StringUrlModel>
)