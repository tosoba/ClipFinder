package com.example.there.data.entities.spotify

import com.google.gson.annotations.SerializedName

data class AlbumData(
        @SerializedName("album_type") val albumType: String,
        val artists: List<SimplifiedArtistData>,
        val id: String,
        @SerializedName("images") val icons: List<IconData>,
        val name: String
)