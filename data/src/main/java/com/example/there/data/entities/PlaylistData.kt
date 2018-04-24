package com.example.there.data.entities

import com.google.gson.annotations.SerializedName

data class PlaylistData(
        val id: String,
        @SerializedName("images") val icons: List<IconData>,
        val name: String
)