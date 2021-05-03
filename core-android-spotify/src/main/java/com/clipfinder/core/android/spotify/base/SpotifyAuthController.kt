package com.clipfinder.core.android.spotify.base

interface SpotifyAuthController {
    var onLoginSuccessful: (() -> Unit)?
    fun showLoginDialog()
}