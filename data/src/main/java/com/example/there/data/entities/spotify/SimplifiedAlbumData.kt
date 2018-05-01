package com.example.there.data.entities.spotify

import com.google.gson.annotations.SerializedName

data class SimplifiedAlbumData(
        val id: String,
        val name: String,
        @SerializedName("images") val icons: List<IconData>
)