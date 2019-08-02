package com.example.spotifyrepo

import android.util.Base64
import com.example.core.retrofit.NetworkResponse
import com.example.coreandroid.preferences.SpotifyPreferences
import com.example.coreandroid.repo.BaseRemoteRepo
import com.example.spotifyapi.SpotifyAccountsApi
import com.example.spotifyapi.SpotifyAuth
import com.example.spotifyapi.model.AccessTokenApiModel
import com.example.spotifyapi.model.SpotifyAuthErrorApiModel
import com.example.spotifyrepo.mapper.domain
import com.example.spotifyrepo.util.observable
import com.example.spotifyrepo.util.single
import io.reactivex.Observable
import io.reactivex.Single

abstract class BaseSpotifyRemoteRepo(
        private val accountsApi: SpotifyAccountsApi,
        protected val preferences: SpotifyPreferences
) : BaseRemoteRepo() {

    private val clientDataHeader: String
        get() {
            val encoded = Base64.encodeToString("${SpotifyAuth.id}:${SpotifyAuth.secret}".toByteArray(), Base64.NO_WRAP)
            return "Basic $encoded"
        }

    private val accessToken: Single<NetworkResponse<AccessTokenApiModel, SpotifyAuthErrorApiModel>>
        get() = accountsApi.getAccessToken(authorization = clientDataHeader)

    protected fun <T> withTokenObservable(
            block: (String) -> Observable<T>
    ): Observable<T> = preferences.accessToken.observable.loadIfNeededThenFlatMapValid(block)

    protected fun <T> withTokenSingle(
            block: (String) -> Single<T>
    ): Single<T> = preferences.accessToken.single.loadIfNeededThenFlatMapValid(block)

    protected fun <T> Observable<SpotifyPreferences.SavedAccessTokenEntity>.loadIfNeededThenFlatMapValid(
            block: (String) -> Observable<T>
    ): Observable<T> = flatMap { saved ->
        when (saved) {
            is SpotifyPreferences.SavedAccessTokenEntity.Valid -> block(saved.token)
            else -> accessToken.toObservable()
                    .mapToDataOrThrow(AccessTokenApiModel::domain)
                    .doOnNext { preferences.accessToken = it }
                    .map { it.token }
                    .flatMap(block)
        }
    }

    protected fun <T> Observable<SpotifyPreferences.SavedAccessTokenEntity>.flatMapValidElseThrow(
            block: (String) -> Observable<T>
    ): Observable<T> = flatMap { saved ->
        when (saved) {
            is SpotifyPreferences.SavedAccessTokenEntity.Valid -> block(saved.token)
            else -> Observable.error { IllegalStateException(ACCESS_TOKEN_UNAVAILABLE) }
        }
    }

    protected fun <T> Single<SpotifyPreferences.SavedAccessTokenEntity>.loadIfNeededThenFlatMapValid(
            block: (String) -> Single<T>
    ): Single<T> = flatMap { saved ->
        when (saved) {
            is SpotifyPreferences.SavedAccessTokenEntity.Valid -> block(saved.token)
            else -> accessToken
                    .mapToDataOrThrow(AccessTokenApiModel::domain)
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