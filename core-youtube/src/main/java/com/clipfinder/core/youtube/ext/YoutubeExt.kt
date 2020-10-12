package com.clipfinder.core.youtube.ext

import com.google.api.client.googleapis.services.AbstractGoogleClientRequest
import io.reactivex.Single

val <T> AbstractGoogleClientRequest<T>.single: Single<T>
    get() = Single.just(execute())
