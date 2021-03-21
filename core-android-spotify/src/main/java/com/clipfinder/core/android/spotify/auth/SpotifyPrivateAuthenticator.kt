package com.clipfinder.core.android.spotify.auth

import com.clipfinder.core.android.spotify.preferences.SpotifyPreferences
import com.clipfinder.core.spotify.auth.ISpotifyPrivateAuthenticator
import com.clipfinder.core.spotify.ext.authorizedWith
import net.openid.appauth.AuthorizationService
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.util.concurrent.CountDownLatch

class SpotifyPrivateAuthenticator(
    private val preferences: SpotifyPreferences,
    private val authorizationService: AuthorizationService
) : ISpotifyPrivateAuthenticator {
    override fun authenticate(route: Route?, response: Response): Request = authorizePrivate(response)

    @Synchronized
    private fun authorizePrivate(response: Response): Request {
        val authState = preferences.authState ?: throw NullAuthStateException
        val refreshTokenLatch = CountDownLatch(1)
        var refreshedToken: String? = null
        authState.performActionWithFreshTokens(authorizationService) { accessToken, _, ex ->
            when {
                ex != null -> throw ex
                accessToken != null -> {
                    refreshedToken = accessToken
                    refreshTokenLatch.countDown()
                }
                else -> throw UnknownRefreshTokenRequestException
            }
        }
        refreshTokenLatch.await()

        val accessToken = requireNotNull(refreshedToken) { "Refreshed token is null" }
        preferences.authState = authState
        return response.request.authorizedWith(accessToken)
    }

    object NullAuthStateException : Throwable()
    object UnknownRefreshTokenRequestException : Throwable()
}
