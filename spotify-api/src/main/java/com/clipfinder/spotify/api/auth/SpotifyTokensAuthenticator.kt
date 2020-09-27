package com.clipfinder.spotify.api.auth

import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class SpotifyTokensAuthenticator : okhttp3.Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        TODO("Not yet implemented")
    }
}
