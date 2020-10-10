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

fun <T : Any, E : Any, R> Observable<NetworkResponse<T, E>>.mapSuccess(
    finisher: T.() -> R
): Observable<R> = flatMap { response ->
    when (response) {
        is NetworkResponse.Success -> Observable.just(response.body.finisher())
        is NetworkResponse.NetworkError -> Observable.error(response.error)
        is NetworkResponse.ServerError -> Observable.error(ThrowableServerError(response))
        is NetworkResponse.DifferentError -> Observable.error(response.error)
    }
}

fun <T : Any, E : Any, R> Single<NetworkResponse<T, E>>.mapSuccess(
    finisher: T.() -> R
): Single<R> = flatMap { response ->
    when (response) {
        is NetworkResponse.Success -> Single.just(response.body.finisher())
        is NetworkResponse.NetworkError -> Single.error(response.error)
        is NetworkResponse.ServerError -> Single.error(ThrowableServerError(response))
        is NetworkResponse.DifferentError -> Single.error(response.error)
    }
}

fun <T : Any, E : Any> Single<NetworkResponse<T, E>>.mapSuccess(): Single<T> = flatMap { response ->
    when (response) {
        is NetworkResponse.Success -> Single.just(response.body)
        is NetworkResponse.NetworkError -> Single.error(response.error)
        is NetworkResponse.ServerError -> Single.error(ThrowableServerError(response))
        is NetworkResponse.DifferentError -> Single.error(response.error)
    }
}
