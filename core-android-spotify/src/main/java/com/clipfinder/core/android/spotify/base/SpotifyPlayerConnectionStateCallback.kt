package com.clipfinder.core.android.spotify.base

import com.spotify.sdk.android.player.ConnectionStateCallback
import com.spotify.sdk.android.player.Error

interface SpotifyPlayerConnectionStateCallback : ConnectionStateCallback {
    override fun onLoggedIn() = Unit
    override fun onLoggedOut() = Unit
    override fun onLoginFailed(error: Error?) = Unit
    override fun onTemporaryError() = Unit
    override fun onConnectionMessage(message: String?) = Unit
}
