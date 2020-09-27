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

class ThrowableServerError(
    val error: NetworkResponse.ServerError<*>
) : Throwable("${error.code ?: "No error code"} : ${error.body ?: "No error body."}")

fun <T : Any, E : Any, R> Observable<NetworkResponse<T, E>>.mapToResource(
    mapBody: T.() -> R
): Observable<Resource<R>> = map(resourceMapper(mapBody))

fun <T : Any, E : Any, R> Single<NetworkResponse<T, E>>.mapToResource(
    mapBody: T.() -> R
): Single<Resource<R>> = map(resourceMapper(mapBody))

private fun <T : Any, E : Any, R> resourceMapper(
    mapBody: T.() -> R
): (NetworkResponse<T, E>) -> Resource<R> = { response ->
    when (response) {
        is NetworkResponse.Success -> Resource.Success(response.body.mapBody())
        is NetworkResponse.ServerError -> Resource.Error(response.body)
        is NetworkResponse.NetworkError -> Resource.Error(response.error)
        is NetworkResponse.DifferentError -> Resource.Error(response.error)
    }
}

fun <T : Any, E : Any, R> Observable<NetworkResponse<T, E>>.mapSuccessOrThrow(
    finisher: T.() -> R
): Observable<R> = map(throwingResourceMapper(finisher))

fun <T : Any, E : Any, R> Single<NetworkResponse<T, E>>.mapSuccessOrThrow(
    finisher: T.() -> R
): Single<R> = map(throwingResourceMapper(finisher))

fun <T : Any, E : Any> Single<NetworkResponse<T, E>>.successOrThrow(): Single<T> = map { response ->
    when (response) {
        is NetworkResponse.Success -> response.body
        is NetworkResponse.NetworkError -> throw response.error
        is NetworkResponse.ServerError -> throw ThrowableServerError(response)
        is NetworkResponse.DifferentError -> throw response.error
    }
}

private fun <T : Any, E : Any, R> throwingResourceMapper(
    finisher: T.() -> R
): (NetworkResponse<T, E>) -> R = { response ->
    when (response) {
        is NetworkResponse.Success -> response.body.finisher()
        is NetworkResponse.NetworkError -> throw response.error
        is NetworkResponse.ServerError -> throw ThrowableServerError(response)
        is NetworkResponse.DifferentError -> throw response.error
    }
}
