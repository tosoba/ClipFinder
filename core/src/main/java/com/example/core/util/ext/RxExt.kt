package com.example.core.util.ext

import io.reactivex.Maybe
import io.reactivex.Single

fun <T> Maybe<T>.mapToSingleBoolean(): Single<Boolean> = this
        .map { true }
        .defaultIfEmpty(false)
        .toSingle()