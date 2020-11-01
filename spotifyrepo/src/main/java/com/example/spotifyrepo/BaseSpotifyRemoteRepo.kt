package com.example.spotifyrepo

import com.example.core.android.spotify.preferences.SpotifyPreferences
import io.reactivex.Single

abstract class BaseSpotifyRemoteRepo(
    protected val preferences: SpotifyPreferences
) {
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
