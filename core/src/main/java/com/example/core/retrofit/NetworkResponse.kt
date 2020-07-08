package com.example.core.retrofit

import com.example.core.model.Resource
import io.reactivex.Observable
import io.reactivex.Single
import java.io.IOException

sealed class NetworkResponse<out T : Any, out E : Any> {
    data class Success<T : Any>(val body: T) : NetworkResponse<T, Nothing>()
    data class ServerError<E : Any>(val body: E?, val code: Int?) : NetworkResponse<Nothing, E>()
    data class NetworkError(val error: IOException) : NetworkResponse<Nothing, Nothing>()
    data class DifferentError(val error: Throwable) : NetworkResponse<Nothing, Nothing>()
}

class ThrowableServerError(val error: NetworkResponse.ServerError<*>) : Throwable()

fun <T : Any, E : Any, R> Observable<NetworkResponse<T, E>>.mapToResource(
    finisher: T.() -> R
): Observable<Resource<R>> = map(resourceMapper(finisher))

fun <T : Any, E : Any, R> Single<NetworkResponse<T, E>>.mapToResource(
    finisher: T.() -> R
): Single<Resource<R>> = map(resourceMapper(finisher))

private fun <T : Any, E : Any, R> resourceMapper(
    finisher: T.() -> R
): (NetworkResponse<T, E>) -> Resource<R> = { response ->
    when (response) {
        is NetworkResponse.Success -> Resource.Success(response.body.finisher())
        is NetworkResponse.ServerError -> Resource.Error(response.body)
        is NetworkResponse.NetworkError -> Resource.Error(response.error)
        is NetworkResponse.DifferentError -> Resource.Error(response.error)
    }
}

fun <T : Any, E : Any, R> Observable<NetworkResponse<T, E>>.mapToDataOrThrow(
    finisher: T.() -> R
): Observable<R> = map(throwingResourceMapper(finisher))

fun <T : Any, E : Any, R> Single<NetworkResponse<T, E>>.mapToDataOrThrow(
    finisher: T.() -> R
): Single<R> = map(throwingResourceMapper(finisher))

private fun <T : Any, E : Any, R> throwingResourceMapper(
    finisher: T.() -> R
): (NetworkResponse<T, E>) -> R = { response ->
    when (response) {
        is NetworkResponse.Success -> Resource.Success(response.body.finisher()).data
        is NetworkResponse.NetworkError -> throw response.error
        is NetworkResponse.ServerError -> throw ThrowableServerError(response)
        is NetworkResponse.DifferentError -> throw response.error
    }
}
