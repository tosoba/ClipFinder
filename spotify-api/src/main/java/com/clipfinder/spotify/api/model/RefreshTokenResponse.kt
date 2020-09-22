package com.clipfinder.spotify.api.model

import com.squareup.moshi.Json

data class RefreshTokenResponse(
    @Json(name = "access_token")
    val accessToken: String,
    @Json(name = "token_type")
    val tokenType: String,
    @Json(name = "expires_in")
    val expiresIn: Int,
    @Json(name = "refresh_token")
    val refreshToken: String,
    val scope: String
)