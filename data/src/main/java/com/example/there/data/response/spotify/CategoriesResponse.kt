package com.example.there.data.response.spotify

import com.example.there.data.entities.spotify.CategoryData
import com.google.gson.annotations.SerializedName

data class CategoriesResponse(
        @SerializedName("categories") val result: CategoriesResult
)

data class CategoriesResult(
        @SerializedName("href") val url: String,
        @SerializedName("items") val categories: List<CategoryData>
)