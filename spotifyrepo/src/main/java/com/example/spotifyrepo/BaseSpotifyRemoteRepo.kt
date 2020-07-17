package com.example.spotifyrepo

import com.example.core.SpotifyDefaults
import com.example.core.android.spotify.api.ext.accessToken
import com.example.core.android.spotify.model.ext.observable
import com.example.core.android.spotify.model.ext.single
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.retrofit.NetworkResponse
import com.example.core.retrofit.mapToDataOrThrow
import com.example.spotifyapi.SpotifyAccountsApi
import com.example.spotifyapi.model.AccessTokenApiModel
import com.example.spotifyapi.model.PagedResponse
import com.example.spotifyrepo.mapper.domain
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

abstract class BaseSpotifyRemoteRepo(
    private val accountsApi: SpotifyAccountsApi,
    protected val preferences: SpotifyPreferences
) {

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
            else -> accountsApi.accessToken
                .toObservable()
                .mapToDataOrThrow(AccessTokenApiModel::domain)
                .doOnNext { preferences.accessToken = it }
                .map { it.token }
                .flatMap(block)
        }
    }

    protected fun <R : PagedResponse<T>, E : Any, T> getAllItems(
        request: (String, Int) -> Observable<NetworkResponse<R, E>>
    ): Observable<NetworkResponse<R, E>> {
        val offsetSubject = BehaviorSubject.createDefault(0)
        return offsetSubject.concatMap { offset ->
            withTokenObservable { token -> request(token, offset) }
        }.doOnNext {
            when (it) {
                is NetworkResponse.Success -> {
                    if (it.body.offset < it.body.totalItems - SpotifyDefaults.LIMIT)
                        offsetSubject.onNext(it.body.offset + SpotifyDefaults.LIMIT)
                    else offsetSubject.onComplete()
                }
                //TODO: this swallows exceptions... - can result in "partial" responses - for ex. one success followed by error it should probably be replaced with onError()
                else -> offsetSubject.onComplete()
            }
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
            else -> accountsApi.accessToken
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
