package com.example.core.android.spotify.model.ext

import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.there.domain.entity.spotify.AccessTokenEntity
import io.reactivex.Observable
import io.reactivex.Single

val AccessTokenEntity?.single: Single<SpotifyPreferences.SavedAccessTokenEntity>
    get() = when {
        this == null -> Single.just(SpotifyPreferences.SavedAccessTokenEntity.NoValue)
        !isValid -> Single.just(SpotifyPreferences.SavedAccessTokenEntity.Invalid)
        else -> Single.just(SpotifyPreferences.SavedAccessTokenEntity.Valid(token))
    }
