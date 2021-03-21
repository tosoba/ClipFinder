package com.clipfinder.core.spotify.auth

import io.reactivex.Completable

interface ISpotifyAutoAuth {
    fun authorizePrivate(): Completable
}