package com.example.spotifyrepo.util

import com.example.core.model.StringUrlModel
import com.example.coreandroid.preferences.SpotifyPreferences
import com.example.there.domain.entity.spotify.AccessTokenEntity
import io.reactivex.Observable
import io.reactivex.Single

private const val DEFAULT_ICON_URL = "https://t.scdn.co/media/derived/r-b-274x274_fd56efa72f4f63764b011b68121581d8_0_0_274_274.jpg"

val List<StringUrlModel>.firstIconUrlOrDefault: String
    get() = getOrNull(0)?.url ?: DEFAULT_ICON_URL

val List<StringUrlModel>.secondIconUrlOrDefault: String
    get() = getOrNull(1)?.url ?: getOrNull(0)?.url ?: DEFAULT_ICON_URL

val AccessTokenEntity?.single: Single<SpotifyPreferences.SavedAccessTokenEntity>
    get() = when {
        this == null -> Single.just(SpotifyPreferences.SavedAccessTokenEntity.NoValue)
        !isValid -> Single.just(SpotifyPreferences.SavedAccessTokenEntity.Invalid)
        else -> Single.just(SpotifyPreferences.SavedAccessTokenEntity.Valid(token))
    }

val AccessTokenEntity?.observable: Observable<SpotifyPreferences.SavedAccessTokenEntity>
    get() = when {
        this == null -> Observable.just(SpotifyPreferences.SavedAccessTokenEntity.NoValue)
        !isValid -> Observable.just(SpotifyPreferences.SavedAccessTokenEntity.Invalid)
        else -> Observable.just(SpotifyPreferences.SavedAccessTokenEntity.Valid(token))
    }
