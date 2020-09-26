package com.example.core.android.spotify.api

import android.util.Base64
import com.clipfinder.spotify.api.endpoint.AuthEndpoints
import com.clipfinder.spotify.api.model.GrantType
import com.example.core.android.spotify.api.ext.accessToken
import com.example.core.android.spotify.api.ext.domain
import com.example.core.android.spotify.model.ext.observable
import com.example.core.android.spotify.model.ext.single
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.ext.RxSchedulers
import com.example.core.retrofit.mapSuccessOrThrow
import com.example.core.retrofit.successOrThrow
import com.example.spotifyapi.SpotifyAccountsApi
import com.example.spotifyapi.model.AccessTokenApiModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class SpotifyAuth(
    private val accountsApi: SpotifyAccountsApi,
    private val preferences: SpotifyPreferences,
    private val authEndpoints: AuthEndpoints,
    private val rxSchedulers: RxSchedulers
) {
    fun authorize(): Completable = Single.just(preferences.hasTokens)
        .flatMapCompletable { hasTokens ->
            if (hasTokens) Completable.complete()
            else authEndpoints
                .getTokens(
                    grantType = GrantType.CLIENT_CREDENTIALS,
                    clientId = ID
                )
                .subscribeOn(rxSchedulers.io)
                .successOrThrow()
                .observeOn(rxSchedulers.main)
                .doOnSuccess { preferences.setTokens(it.accessToken, it.refreshToken) }
                .toCompletable()
        }

    fun requirePrivateAuthorized(): Completable = Single
        .just(preferences.hasTokens && preferences.tokensPrivate)
        .flatMapCompletable { privateAuthorized ->
            if (privateAuthorized) Completable.complete()
            else Completable.error(UnauthorizedException())
        }

    fun <T> withTokenObservable(
        private: Boolean = false,
        block: (String) -> Observable<T>
    ): Observable<T> = preferences
        .run { if (private) userPrivateAccessToken else accessToken }
        .observable
        .run { if (private) flatMapValidElseThrow(block) else loadIfNeededThenFlatMapValid(block) }

    fun <T> withTokenSingle(
        private: Boolean = false,
        block: (String) -> Single<T>
    ): Single<T> = preferences
        .run { if (private) userPrivateAccessToken else accessToken }
        .single
        .run { if (private) flatMapValidElseThrow(block) else loadIfNeededThenFlatMapValid(block) }

    private fun <T> Observable<SpotifyPreferences.SavedAccessTokenEntity>.loadIfNeededThenFlatMapValid(
        block: (String) -> Observable<T>
    ): Observable<T> = flatMap { saved ->
        when (saved) {
            is SpotifyPreferences.SavedAccessTokenEntity.Valid -> block("Bearer ${saved.token}")
            else -> accountsApi.accessToken
                .toObservable()
                .mapSuccessOrThrow(AccessTokenApiModel::domain)
                .doOnNext { preferences.accessToken = it }
                .flatMap { block("Bearer ${it.token}") }
        }
    }

    private fun <T> Single<SpotifyPreferences.SavedAccessTokenEntity>.loadIfNeededThenFlatMapValid(
        block: (String) -> Single<T>
    ): Single<T> = flatMap { saved ->
        when (saved) {
            is SpotifyPreferences.SavedAccessTokenEntity.Valid -> block("Bearer ${saved.token}")
            else -> accountsApi.accessToken
                .mapSuccessOrThrow(AccessTokenApiModel::domain)
                .doOnSuccess { preferences.accessToken = it }
                .flatMap { block("Bearer ${it.token}") }
        }
    }

    private fun <T> Observable<SpotifyPreferences.SavedAccessTokenEntity>.flatMapValidElseThrow(
        block: (String) -> Observable<T>
    ): Observable<T> = flatMap { saved ->
        when (saved) {
            is SpotifyPreferences.SavedAccessTokenEntity.Valid -> block("Bearer ${saved.token}")
            is SpotifyPreferences.SavedAccessTokenEntity.NoValue -> Observable
                .error(IllegalStateException("No private access token granted."))
            is SpotifyPreferences.SavedAccessTokenEntity.Invalid -> Observable
                .error(AccessTokenExpiredException())
        }
    }

    private fun <T> Single<SpotifyPreferences.SavedAccessTokenEntity>.flatMapValidElseThrow(
        block: (String) -> Single<T>
    ): Single<T> = flatMap { saved ->
        when (saved) {
            is SpotifyPreferences.SavedAccessTokenEntity.Valid -> block("Bearer ${saved.token}")
            is SpotifyPreferences.SavedAccessTokenEntity.NoValue -> Single
                .error(IllegalStateException("No private access token granted."))
            is SpotifyPreferences.SavedAccessTokenEntity.Invalid -> Single
                .error(AccessTokenExpiredException())
        }
    }

    class AccessTokenExpiredException : Throwable("Access token has expired.")

    class UnauthorizedException : Throwable("Private authorization is required.")

    companion object {
        const val ID = "6dc5e6590b8b48c5bd73a64f6c206d8a"
        private const val SECRET = "d5c30ea11b90401980c6ca37dc0512ba"
        const val REDIRECT_URI = "testschema://callback"

        val scopes = arrayOf(
            "user-read-email",
            "user-read-private",
            "user-library-read",
            "user-top-read",
            "playlist-read-collaborative",
            "playlist-read-private",
            "streaming"
        )

        internal val clientDataHeader: String = "Basic ${Base64.encodeToString("$ID:$SECRET".toByteArray(), Base64.NO_WRAP)}"
    }
}
