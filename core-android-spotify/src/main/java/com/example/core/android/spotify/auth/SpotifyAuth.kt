package com.example.core.android.spotify.auth

import android.util.Base64
import com.clipfinder.core.spotify.auth.ISpotifyAuth
import com.clipfinder.spotify.api.endpoint.TokenEndpoints
import com.clipfinder.spotify.api.model.GrantType
import com.clipfinder.spotify.api.model.TokensResponse
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.ext.mapSuccess
import io.reactivex.Completable
import io.reactivex.Single

class SpotifyAuth(
    private val clientId: String,
    private val clientSecret: String,
    private val preferences: SpotifyPreferences,
    private val tokenEndpoints: TokenEndpoints
) : ISpotifyAuth {

    override fun authorize(): Completable = Single.just(preferences.hasTokens)
        .flatMapCompletable { hasTokens ->
            if (hasTokens) Completable.complete()
            else tokenEndpoints
                .getTokens(
                    authorization = "Basic ${Base64.encodeToString("${clientId}:${clientSecret}".toByteArray(), Base64.NO_WRAP)}",
                    grantType = GrantType.CLIENT_CREDENTIALS
                )
                .mapSuccess(TokensResponse::accessToken)
                .doOnSuccess(preferences::setToken)
                .toCompletable()
        }

    override fun requirePrivateAuthorized(): Completable = Single
        .just(preferences.hasTokens && preferences.tokensPrivate)
        .flatMapCompletable { privateAuthorized ->
            if (privateAuthorized) Completable.complete()
            else Completable.error(UnauthorizedException())
        }

    class UnauthorizedException : Throwable("Private authorization is required.")
}
