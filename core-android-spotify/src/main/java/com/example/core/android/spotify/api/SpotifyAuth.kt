package com.example.core.android.spotify.api

import android.util.Base64
import com.example.core.android.spotify.api.ext.accessToken
import com.example.core.android.spotify.api.ext.domain
import com.example.core.android.spotify.model.ext.observable
import com.example.core.android.spotify.model.ext.single
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.retrofit.mapSuccessOrThrow
import com.example.spotifyapi.SpotifyAccountsApi
import com.example.spotifyapi.model.AccessTokenApiModel
import io.reactivex.Observable
import io.reactivex.Single

class SpotifyAuth(
    private val accountsApi: SpotifyAccountsApi,
    private val preferences: SpotifyPreferences
) {
    fun <T> withTokenObservable(
        block: (String) -> Observable<T>
    ): Observable<T> = preferences.accessToken
        .observable
        .loadIfNeededThenFlatMapValid(block)

    fun <T> withTokenSingle(
        block: (String) -> Single<T>
    ): Single<T> = preferences.accessToken
        .single
        .loadIfNeededThenFlatMapValid(block)

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

    companion object {
        const val ID = "6dc5e6590b8b48c5bd73a64f6c206d8a"
        private const val SECRET = "d5c30ea11b90401980c6ca37dc0512ba"
        const val REDIRECT_URI = "testschema://callback"

        val scopes = arrayOf(
            "user-read-private",
            "user-library-read",
            "user-top-read",
            "playlist-read",
            "playlist-read-private",
            "streaming",
            "user-read-birthdate",
            "user-read-email"
        )

        internal val clientDataHeader: String by lazy {
            val encoded = Base64.encodeToString("$ID:$SECRET".toByteArray(), Base64.NO_WRAP)
            "Basic $encoded"
        }
    }
}
