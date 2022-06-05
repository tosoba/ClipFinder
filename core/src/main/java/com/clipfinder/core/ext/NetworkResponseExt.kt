package com.clipfinder.core.ext

import com.clipfinder.core.model.NetworkResponse
import com.clipfinder.core.model.Resource
import com.clipfinder.core.model.ThrowableServerError
import io.reactivex.Single

fun <T : Any, E : Any, R> Single<NetworkResponse<T, E>>.mapToResource(
    mapBody: T.() -> R
): Single<Resource<R>> = map { it.toResource(mapBody) }

val <T : R, E : Any, R : Any> Single<NetworkResponse<T, E>>.resource: Single<Resource<R>>
    get() = map(NetworkResponse<T, E>::resource)

private fun <T : Any, E : Any, R> NetworkResponse<T, E>.toResource(
    mapBody: T.() -> R
): Resource<R> =
    when (this) {
        is NetworkResponse.Success -> Resource.Success(body.mapBody())
        is NetworkResponse.ServerError -> Resource.Error(body)
        is NetworkResponse.NetworkError -> Resource.Error(error)
        is NetworkResponse.DifferentError -> Resource.Error(error)
    }

private val <T : R, E : Any, R : Any> NetworkResponse<T, E>.resource: Resource<R>
    get() =
        when (this) {
            is NetworkResponse.Success -> Resource.Success(body)
            is NetworkResponse.ServerError -> Resource.Error(body)
            is NetworkResponse.NetworkError -> Resource.Error(error)
            is NetworkResponse.DifferentError -> Resource.Error(error)
        }

fun <T : Any, E : Any, R> Single<NetworkResponse<T, E>>.mapSuccess(finisher: T.() -> R): Single<R> =
    flatMap { response ->
        when (response) {
            is NetworkResponse.Success -> Single.just(response.body.finisher())
            is NetworkResponse.NetworkError -> Single.error(response.error)
            is NetworkResponse.ServerError -> Single.error(ThrowableServerError(response))
            is NetworkResponse.DifferentError -> Single.error(response.error)
        }
    }

val <T : Any, E : Any> Single<NetworkResponse<T, E>>.success: Single<T>
    get() = flatMap { response ->
        when (response) {
            is NetworkResponse.Success -> Single.just(response.body)
            is NetworkResponse.NetworkError -> Single.error(response.error)
            is NetworkResponse.ServerError -> Single.error(ThrowableServerError(response))
            is NetworkResponse.DifferentError -> Single.error(response.error)
        }
    }
