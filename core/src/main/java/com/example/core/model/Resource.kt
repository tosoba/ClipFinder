package com.example.core.model

import io.reactivex.Observable
import io.reactivex.Single

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error<out T, out E>(val error: E, val data: T? = null) : Resource<T>()

    fun <S> map(mapper: (T) -> S): Resource<S> = when (this) {
        is Success<T> -> Success(mapper(data))
        is Error<T, *> -> Error(error, data?.let { mapper(it) })
    }
}

fun <T, S> Single<Resource<T>>.mapData(mapper: (T) -> S): Single<Resource<S>> = map { it.map(mapper) }

fun <T, S> Observable<Resource<T>>.mapData(mapper: (T) -> S): Observable<Resource<S>> = map {
    it.map(mapper)
}
