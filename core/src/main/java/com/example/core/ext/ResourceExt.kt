package com.example.core.ext

import com.example.core.model.Resource
import io.reactivex.Observable
import io.reactivex.Single

fun <T, S> Single<Resource<T>>.mapData(mapper: (T) -> S): Single<Resource<S>> = map {
    it.map(mapper)
}

fun <T, S> Observable<Resource<T>>.mapData(mapper: (T) -> S): Observable<Resource<S>> = map {
    it.map(mapper)
}
