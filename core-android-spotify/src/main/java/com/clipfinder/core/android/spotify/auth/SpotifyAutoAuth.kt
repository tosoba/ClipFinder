package com.clipfinder.core.android.spotify.auth

import com.clipfinder.core.android.spotify.preferences.SpotifyPreferences
import com.clipfinder.core.ext.mapSuccess
import com.clipfinder.core.spotify.auth.ISpotifyAutoAuth
import com.clipfinder.spotify.api.endpoint.TokenEndpoints
import com.clipfinder.spotify.api.model.GrantType
import com.clipfinder.spotify.api.model.TokensResponse
import io.reactivex.Completable
import io.reactivex.Single
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationService

class SpotifyAutoAuth(
    private val authorization: String,
    private val authService: AuthorizationService,
    private val preferences: SpotifyPreferences,
    private val tokenEndpoints: TokenEndpoints
) : ISpotifyAutoAuth {
    override fun authorizePublic(): Completable = Single.just(preferences.hasTokens)
        .flatMapCompletable { hasTokens ->
            if (hasTokens) Completable.complete()
            else tokenEndpoints
                .getTokens(authorization = authorization, grantType = GrantType.CLIENT_CREDENTIALS)
                .mapSuccess(TokensResponse::accessToken)
                .doOnSuccess { preferences.publicAccessToken = it }
                .toCompletable()
        }

    override fun authorizePrivate(): Completable = Completable.create { emitter ->
        val authState = preferences.authState
        if (authState == null) {
            emitter.onError(NullAuthStateException)
            return@create
        }

        try {
            authState.performActionWithFreshTokens(
                authService,
                AuthState.AuthStateAction { accessToken, _, ex ->
                    when {
                        ex != null -> emitter.onError(ex)
                        accessToken != null -> emitter.onComplete()
                        else -> emitter.onError(UnknownRefreshTokenRequestException)
                    }
                }
            )
        } catch (ex: Exception) {
            emitter.onError(ex)
        }
    }

    object NullAuthStateException : Throwable()
    object UnknownRefreshTokenRequestException : Throwable()
}
