package com.example.core.model

import io.reactivex.Observable
import io.reactivex.Single

/**
 * A sealed class to represent UI states associated with a resource.
 */
sealed class Resource<out T> {
    /**
     * A data class to represent the scenario where the resource is available without any errors
     */
    data class Success<out T>(val data: T) : Resource<T>()

    /**
     * A data class to represent the scenario where a resource may or may not be available due to an error
     */
    data class Error<out T, out E>(val error: E, val data: T? = null) : Resource<T>()

    fun <S> map(mapper: (T) -> S): Resource<S> = when (this) {
        is Success<T> -> Success(mapper(data))
        is Error<T, *> -> Error(error, data?.let { mapper(it) })
    }
}

fun <T, S> Single<Resource<T>>.mapData(
        mapper: (T) -> S
): Single<Resource<S>> = map { it.map(mapper) }

fun <T, S> Observable<Resource<T>>.mapData(
        mapper: (T) -> S
): Observable<Resource<S>> = map { it.map(mapper) }



