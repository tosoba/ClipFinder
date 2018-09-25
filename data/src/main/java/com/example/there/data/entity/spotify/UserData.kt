package com.example.there.data.entity.spotify

import com.google.gson.annotations.SerializedName

data class UserData(
        @SerializedName("id")
        val name: String,

        @SerializedName("images")
        val icons: List<IconData>
)