package com.example.core.android.spotify.api

import android.util.Base64
import com.clipfinder.core.spotify.auth.ISpotifyAuth
import com.clipfinder.spotify.api.endpoint.TokenEndpoints
import com.clipfinder.spotify.api.model.GrantType
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.retrofit.mapSuccess
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
                .mapSuccess()
                .doOnSuccess { preferences.setToken(it.accessToken) }
                .toCompletable()
        }

    override fun requirePrivateAuthorized(): Completable = Single
        .just(preferences.hasTokens && preferences.tokensPrivate)
        .flatMapCompletable { privateAuthorized ->
            if (privateAuthorized) Completable.complete()
            else Completable.error(UnauthorizedException())
        }

    class UnauthorizedException : Throwable("Private authorization is required.")

    companion object {
        private const val ID = "6dc5e6590b8b48c5bd73a64f6c206d8a"
        private const val SECRET = "d5c30ea11b90401980c6ca37dc0512ba"

        internal val clientDataHeader: String = "Basic ${Base64.encodeToString("$ID:$SECRET".toByteArray(), Base64.NO_WRAP)}"
    }
}
