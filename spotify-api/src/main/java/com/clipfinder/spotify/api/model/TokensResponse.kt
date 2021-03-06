package com.clipfinder.spotify.api.model

import com.squareup.moshi.Json

data class TokensResponse(
    @Json(name = "access_token")
    val accessToken: String,
    @Json(name = "refresh_token")
    val refreshToken: String?,
    @Json(name = "token_type")
    val tokenType: String,
    @Json(name = "expires_in")
    val expiresIn: Int
)