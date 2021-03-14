package com.clipfinder.core.spotify.auth

import io.reactivex.Completable

interface ISpotifyAutoAuth {
    fun authorizePublic(): Completable
    fun authorizePrivate(): Completable
}