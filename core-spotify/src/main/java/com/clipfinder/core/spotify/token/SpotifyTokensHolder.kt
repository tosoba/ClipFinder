package com.clipfinder.core.spotify.token

interface SpotifyTokensHolder : AccessTokenHolder {
    fun setPrivateTokens(accessToken: String, refreshToken: String)
    fun setToken(accessToken: String)
    fun clearTokens()
    val refreshToken: String
    val tokensPrivate: Boolean
}