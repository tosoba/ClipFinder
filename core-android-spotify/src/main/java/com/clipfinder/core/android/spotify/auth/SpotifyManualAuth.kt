package com.clipfinder.core.android.spotify.auth

import android.content.Intent
import android.net.Uri
import com.clipfinder.core.android.spotify.BuildConfig
import com.clipfinder.core.android.spotify.model.SpotifyAuthResponse
import com.clipfinder.core.android.spotify.preferences.SpotifyPreferences
import io.reactivex.Single
import net.openid.appauth.*

class SpotifyManualAuth(
    private val authService: AuthorizationService,
    private val preferences: SpotifyPreferences
) {
    private val authorizationServiceConfiguration: AuthorizationServiceConfiguration
        get() = AuthorizationServiceConfiguration(
            Uri.parse(AUTHORIZATION_ENDPOINT_URL),
            Uri.parse(TOKEN_ENDPOINT_URL)
        )

    val authRequestIntent: Intent
        get() = authService.getAuthorizationRequestIntent(
            AuthorizationRequest
                .Builder(
                    preferences.authState?.authorizationServiceConfiguration
                        ?: authorizationServiceConfiguration,
                    BuildConfig.SPOTIFY_CLIENT_ID,
                    ResponseTypeValues.CODE,
                    Uri.parse(BuildConfig.SPOTIFY_REDIRECT_URI)
                )
                .setScopes(*SCOPES)
                .build()
        )

    fun sendTokenRequestFrom(dataIntent: Intent): Single<SpotifyAuthResponse> = Single
        .create<SpotifyAuthResponse> { emitter ->
            val authResponse = AuthorizationResponse.fromIntent(dataIntent)
            val authException = AuthorizationException.fromIntent(dataIntent)

            val authState = preferences.authState ?: AuthState(authorizationServiceConfiguration)
            authState.update(authResponse, authException)
            preferences.authState = authState

            when {
                authException != null -> emitter.onError(authException)
                authResponse != null -> authService
                    .performTokenRequest(authResponse.createTokenExchangeRequest()) { tokenResponse, tokenException ->
                        authState.update(tokenResponse, tokenException)
                        preferences.authState = authState

                        when {
                            tokenException != null -> emitter.onError(tokenException)
                            tokenResponse != null -> {
                                val accessToken = tokenResponse.accessToken
                                val refreshToken = tokenResponse.refreshToken
                                when {
                                    accessToken == null -> emitter.onError(
                                        IllegalStateException("Received response does not contain an access token.")
                                    )
                                    refreshToken == null -> emitter.onError(
                                        IllegalStateException("Received response does not contain a refresh token.")
                                    )
                                    else -> emitter.onSuccess(SpotifyAuthResponse(accessToken, refreshToken))
                                }
                            }
                            else -> emitter.onError(IllegalStateException("Token response and exception are both null."))
                        }
                    }
                else -> emitter.onError(IllegalStateException("Auth response and exception are both null."))
            }
        }

    companion object {
        private const val AUTHORIZATION_ENDPOINT_URL = "https://accounts.spotify.com/authorize"
        private const val TOKEN_ENDPOINT_URL = "https://accounts.spotify.com/api/token"
        private val SCOPES = arrayOf(
            "user-read-email",
            "user-read-private",
            "user-library-read",
            "user-top-read",
            "playlist-read-collaborative",
            "playlist-read-private",
            "streaming"
        )
    }
}