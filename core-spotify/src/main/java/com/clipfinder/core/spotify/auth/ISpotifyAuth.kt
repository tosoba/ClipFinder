package com.clipfinder.core.spotify.auth

import io.reactivex.Completable

interface ISpotifyAuth {
    fun authorize(): Completable
    fun requirePrivateAuthorized(): Completable
}