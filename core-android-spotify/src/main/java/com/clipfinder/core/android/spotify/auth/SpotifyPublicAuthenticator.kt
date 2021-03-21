package com.clipfinder.core.android.spotify.auth

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.clipfinder.core.android.spotify.preferences.SpotifyPreferences
import com.clipfinder.core.android.spotify.receiver.PublicAccessTokenExpiredBroadcastReceiver
import com.clipfinder.core.android.util.ext.scheduleSingleAlarm
import com.clipfinder.core.ext.mapSuccess
import com.clipfinder.core.spotify.auth.ISpotifyPublicAuthenticator
import com.clipfinder.core.spotify.ext.authorizedWith
import com.clipfinder.spotify.api.endpoint.TokenEndpoints
import com.clipfinder.spotify.api.model.GrantType
import net.openid.appauth.AuthState
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class SpotifyPublicAuthenticator(
    private val authorization: String,
    private val context: Context,
    private val preferences: SpotifyPreferences,
    private val tokenEndpoints: TokenEndpoints
) : ISpotifyPublicAuthenticator {
    override fun authenticate(route: Route?, response: Response): Request = authenticatePublic(response)

    @Synchronized
    private fun authenticatePublic(response: Response): Request {
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
        schedulePublicAccessTokenClearance(tokenResponse.expiresIn)

        return response.request.authorizedWith(tokenResponse.accessToken)
    }

    private fun schedulePublicAccessTokenClearance(expiresInSeconds: Int) {
        context.scheduleSingleAlarm(
            timestampMillis = System.currentTimeMillis() + expiresInSeconds * 1000 - AuthState.EXPIRY_TIME_TOLERANCE_MS,
            pendingIntent = PendingIntent.getBroadcast(
                context,
                1,
                Intent(context, PublicAccessTokenExpiredBroadcastReceiver::class.java),
                0
            )
        )
    }
}