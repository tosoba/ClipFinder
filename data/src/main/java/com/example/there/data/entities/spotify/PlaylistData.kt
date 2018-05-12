package com.example.there.data.entities.spotify

import com.google.gson.annotations.SerializedName

data class PlaylistData(
        val id: String,
        @SerializedName("images") val icons: List<IconData>,
        val name: String,
        val owner: OwnerData
)

data class OwnerData(val id: String)