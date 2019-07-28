package com.example.core.ext

import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.disposables.Disposable

fun <T> Maybe<T>.isPresent(): Single<Boolean> = map { true }
        .defaultIfEmpty(false)
        .toSingle()

fun Disposable.disposeIfNeeded() {
    if (!isDisposed) dispose()
}