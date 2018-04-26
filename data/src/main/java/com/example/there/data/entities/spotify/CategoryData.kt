package com.example.there.data.entities.spotify

import com.google.gson.annotations.SerializedName

data class CategoryData(
        @SerializedName("href") val url: String,
        val icons: List<IconData>,
        val id: String,
        val name: String
)