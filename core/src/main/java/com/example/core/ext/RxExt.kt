package com.example.core.ext

import io.reactivex.Maybe
import io.reactivex.Single

fun <T> Maybe<T>.isPresent(): Single<Boolean> = map { true }
        .defaultIfEmpty(false)
        .toSingle()