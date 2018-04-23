package com.example.there.data.response

import com.example.there.data.entities.CategoryData
import com.google.gson.annotations.SerializedName

data class CategoriesResponse(
        @SerializedName("categories") val result: CategoriesResult
)

data class CategoriesResult(
        @SerializedName("href") val url: String,
        @SerializedName("items") val categories: List<CategoryData>
)