package com.example.spotifydashboard.data.api.model

import com.google.gson.annotations.SerializedName

class CategoriesApiResult(@SerializedName("items") val categories: List<CategoryApiModel>)
