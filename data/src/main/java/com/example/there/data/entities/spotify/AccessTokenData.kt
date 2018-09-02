package com.example.there.data.entities.spotify

import com.google.gson.annotations.SerializedName

data class AccessTokenData(
        @SerializedName("access_token") val accessToken: String,
        @SerializedName("token_type") val tokenType: String,
        @SerializedName("expires_in") val expiresIn: Int
)