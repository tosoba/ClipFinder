package com.example.core.android.spotify.auth

import android.content.Intent
import android.net.Uri
import com.example.core.android.spotify.model.SpotifyAuthResponse
import com.example.core.android.spotify.preferences.SpotifyPreferences
import io.reactivex.Single
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
}