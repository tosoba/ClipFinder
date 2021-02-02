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
    val authRequestIntent: Intent
        get() = authService.getAuthorizationRequestIntent(
            AuthorizationRequest
                .Builder(
                    AuthorizationServiceConfiguration(
                        Uri.parse(AUTHORIZATION_ENDPOINT_URL),
                        Uri.parse(TOKEN_ENDPOINT_URL)
                    ),
                    _root_ide_package_.com.clipfinder.core.android.spotify.BuildConfig.SPOTIFY_CLIENT_ID,
                    ResponseTypeValues.CODE,
                    Uri.parse(_root_ide_package_.com.clipfinder.core.android.spotify.BuildConfig.SPOTIFY_REDIRECT_URI)
                )
                .setScopes(*SCOPES)
                .build()
        )

    fun sendTokenRequestFrom(dataIntent: Intent): Single<SpotifyAuthResponse> = Single
        .create<SpotifyAuthResponse> { emitter ->
            val authResponse = AuthorizationResponse.fromIntent(dataIntent)
            if (authResponse == null) emitter.onError(
                IllegalStateException("Intent does not contain EXTRA_RESPONSE.")
            )
            else authService.performTokenRequest(authResponse.createTokenExchangeRequest()) { response, ex ->
                when {
                    ex != null -> emitter.onError(ex)
                    response != null -> {
                        val accessToken = response.accessToken
                        val refreshToken = response.refreshToken
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
        }
        .doOnSuccess { (accessToken, refreshToken) -> preferences.setPrivateTokens(accessToken, refreshToken) }

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