package com.example.spotifyapi.models

import com.google.gson.annotations.SerializedName

/**
 * Represents a Spotify Token, retrieved through instantiating a [SpotifyAPI]
 *
 * @property accessToken An access token that can be provided in subsequent calls,
 * for example to Spotify Web API services.
 * @property tokenType How the access token may be used: always “Bearer”.
 * @property expiresIn The time period (in seconds) for which the access token is valid.
 * @property refreshToken A token that can be sent to the Spotify Accounts service in place of an authorization code,
 * null if the token was created using a method that does not support token refresh
 * @property scopes A list of scopes granted access for this [accessToken]. An
 * empty list means that the token can only be used to access public information.
 * @property expiresAt The time, in milliseconds, at which this Token expires
 */
data class Token(
        @SerializedName("access_token") val accessToken: String,
        @SerializedName("token_type") val tokenType: String,
        @SerializedName("expires_in") val expiresIn: Int,
        @SerializedName("refresh_token") val refreshToken: String? = null,
        @SerializedName("scope") private val scopeString: String? = null,
        @Transient val scopes: List<SpotifyScope> = scopeString?.let { str ->
            str.split(" ").mapNotNull { scope -> SpotifyScope.values().find { it.uri.equals(scope, true) } }
        } ?: listOf()
) {
    val expiresAt: Long get() = System.currentTimeMillis() + expiresIn * 1000

    fun shouldRefresh(): Boolean = System.currentTimeMillis() > expiresAt
}

data class TokenValidityResponse(val isValid: Boolean, val exception: Exception?)