package com.example.core.ext

import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable

fun <T> Maybe<T>.isPresent(): Single<Boolean> = map { true }
    .defaultIfEmpty(false)
    .toSingle()

fun Disposable.disposeIfNeeded() {
    if (!isDisposed) dispose()
}

fun <T> T.single(): Single<T> = Single.just(this)

fun <T> T.observable(): Observable<T> = Observable.just(this)
