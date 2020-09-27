package com.clipfinder.core.spotify.token

interface SpotifyTokensHolder : AccessTokenHolder {
    fun setPrivateTokens(accessToken: String, refreshToken: String)
    fun setToken(accessToken: String)
    val refreshToken: String
    val tokensPrivate: Boolean
}