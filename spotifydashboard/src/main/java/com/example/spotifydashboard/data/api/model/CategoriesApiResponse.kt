package com.example.spotifydashboard.data.api.model

import com.google.gson.annotations.SerializedName

class CategoriesApiResponse(
        @SerializedName("categories") val result: CategoriesApiResult,
        val offset: Int,
        @SerializedName("total") val totalItems: Int
)