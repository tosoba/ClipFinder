package com.example.spotifyapi.model

import com.example.core.model.StringUrlModel
import com.google.gson.annotations.SerializedName

data class UserApiModel(
        @SerializedName("id")
        val name: String,

        @SerializedName("images")
        val icons: List<StringUrlModel>
)