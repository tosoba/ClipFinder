package com.example.spotifydashboard.data.api.model

import com.example.spotifyapi.model.PagedResponse
import com.google.gson.annotations.SerializedName

class CategoriesApiResponse(
        @SerializedName("categories") val result: CategoriesApiResult,
        override val offset: Int,
        @SerializedName("total") override val totalItems: Int
) : PagedResponse<CategoryApiModel> {
    override val items: List<CategoryApiModel> get() = result.categories
}