package com.clipfinder.core.spotify.token

interface SpotifyTokensHolder : AccessTokenHolder {
    fun setTokens(accessToken: String, refreshToken: String, private: Boolean? = null)
    val refreshToken: String
    val tokensPrivate: Boolean
}