package com.example.core.android.spotify.auth

import android.content.Intent
import android.net.Uri
import com.example.core.android.spotify.preferences.SpotifyPreferences
import io.reactivex.Completable
import net.openid.appauth.*

class SpotifyManualAuth(
    private val clientId: String,
    private val redirectUri: String,
    private val scopes: Array<String>,
    private val authService: AuthorizationService,
    private val authServiceConfig: AuthorizationServiceConfiguration,
    private val preferences: SpotifyPreferences
) {
    val authRequestIntent: Intent
        get() = authService.getAuthorizationRequestIntent(
            AuthorizationRequest
                .Builder(authServiceConfig, clientId, ResponseTypeValues.CODE, Uri.parse(redirectUri))
                .setScopes(*scopes)
                .build()
        )

    class MissingResponseExtraException : Throwable("Intent does not contain EXTRA_RESPONSE.")
    class MissingAccessTokenException : Throwable("Received response does not contain an access token.")
    class MissingRefreshTokenException : Throwable("Received response does not contain a refresh token.")

    fun sendTokenRequestFrom(dataIntent: Intent): Completable = Completable.create {
        val authResponse = AuthorizationResponse.fromIntent(dataIntent)
        if (authResponse == null) {
            it.onError(MissingResponseExtraException())
            return@create
        }

        authService.performTokenRequest(authResponse.createTokenExchangeRequest()) { response, ex ->
            when {
                ex != null -> it.onError(ex)
                response != null -> {
                    val accessToken = response.accessToken
                    val refreshToken = response.refreshToken
                    when {
                        accessToken == null -> it.onError(MissingAccessTokenException())
                        refreshToken == null -> it.onError(MissingRefreshTokenException())
                        else -> preferences.setPrivateTokens(accessToken, refreshToken)
                    }
                }
                else -> it.onError(IllegalStateException("Token response and exception are both null."))
            }
        }
    }
}