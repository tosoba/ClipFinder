package com.example.spotifyrepo

import com.example.core.android.spotify.api.ext.accessToken
import com.example.core.android.spotify.model.ext.single
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.retrofit.mapSuccess
import com.example.spotifyapi.SpotifyAccountsApi
import com.example.spotifyapi.model.AccessTokenApiModel
import com.example.spotifyrepo.mapper.domain
import io.reactivex.Single

abstract class BaseSpotifyRemoteRepo(
    private val accountsApi: SpotifyAccountsApi,
    protected val preferences: SpotifyPreferences
) {
    protected fun <T> withTokenSingle(
        block: (String) -> Single<T>
    ): Single<T> = preferences.accessToken.single.loadIfNeededThenFlatMapValid(block)

    protected fun <T> Single<SpotifyPreferences.SavedAccessTokenEntity>.loadIfNeededThenFlatMapValid(
        block: (String) -> Single<T>
    ): Single<T> = flatMap { saved ->
        when (saved) {
            is SpotifyPreferences.SavedAccessTokenEntity.Valid -> block(saved.token)
            else -> accountsApi.accessToken
                .mapSuccess(AccessTokenApiModel::domain)
                .doOnSuccess { preferences.accessToken = it }
                .map { it.token }
                .flatMap(block)
        }
    }

    protected fun <T> Single<SpotifyPreferences.SavedAccessTokenEntity>.flatMapValidElseThrow(
        block: (String) -> Single<T>
    ): Single<T> = flatMap { saved ->
        when (saved) {
            is SpotifyPreferences.SavedAccessTokenEntity.Valid -> block(saved.token)
            else -> Single.error { IllegalStateException(ACCESS_TOKEN_UNAVAILABLE) }
        }
    }

    protected fun getAccessTokenHeader(accessToken: String): String = "Bearer $accessToken"

    companion object {
        protected const val ACCESS_TOKEN_UNAVAILABLE = "Access token unavailable."
    }
}
