package com.clipfinder.core.android.spotify.auth

import android.util.Log
import com.clipfinder.core.android.spotify.preferences.SpotifyPreferences
import com.clipfinder.core.ext.mapSuccess
import com.clipfinder.core.spotify.auth.ISpotifyPublicAuthenticator
import com.clipfinder.core.spotify.ext.authorizedWith
import com.clipfinder.spotify.api.endpoint.TokenEndpoints
import com.clipfinder.spotify.api.model.GrantType
import com.google.gson.Gson
import net.openid.appauth.AuthState
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class SpotifyPublicAuthenticator(
    private val authorization: String,
    private val preferences: SpotifyPreferences,
    private val tokenEndpoints: TokenEndpoints
) : ISpotifyPublicAuthenticator {
    override fun authenticate(route: Route?, response: Response): Request = authenticatePublic(response)

    @Synchronized
    private fun authenticatePublic(response: Response): Request {
        Log.e("AUTH_PUB", "${response.code}: ${response.message}")

        val accessToken = preferences.publicAccessToken
        if (accessToken != null) return response.request.authorizedWith(accessToken)

        val tokenResponse = tokenEndpoints
            .getTokens(
                authorization = authorization,
                grantType = GrantType.CLIENT_CREDENTIALS
            )
            .mapSuccess()
            .blockingGet()

        preferences.publicAccessToken = tokenResponse.accessToken
        preferences.publicAccessTokenExpiryTimestamp =
            System.currentTimeMillis() + tokenResponse.expiresIn * 1000 - AuthState.EXPIRY_TIME_TOLERANCE_MS

        return response.request.authorizedWith(tokenResponse.accessToken)
    }
}