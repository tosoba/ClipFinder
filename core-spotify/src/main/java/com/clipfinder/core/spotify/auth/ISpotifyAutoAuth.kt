package com.clipfinder.core.spotify.auth

import io.reactivex.Completable

interface ISpotifyAutoAuth {
    fun authorize(): Completable
    fun requirePrivateAuthorized(): Completable
}