package com.clipfinder.core.android.youtube.ext

import com.google.api.client.googleapis.services.AbstractGoogleClientRequest
import io.reactivex.Single

val <T> AbstractGoogleClientRequest<T>.single: Single<T>
    get() = Single.just(execute())
