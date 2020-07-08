package com.example.there.domain.repo.spotify

import io.reactivex.Observable

interface ISpotifyPreferencesRepo {
    fun <T> withTokenObservable(
        block: (String) -> Observable<T>
    ): Observable<T>
}