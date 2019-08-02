package com.example.coreandroid.repo

import com.example.core.model.Resource
import com.example.core.retrofit.NetworkResponse
import com.example.core.retrofit.ThrowableServerError
import io.reactivex.Observable
import io.reactivex.Single

abstract class BaseRemoteRepo {

    protected fun <T : Any, E : Any, R> Observable<NetworkResponse<T, E>>.mapToResource(
            finisher: T.() -> R
    ): Observable<Resource<R>> = map(resourceMapper(finisher))

    protected fun <T : Any, E : Any, R> Single<NetworkResponse<T, E>>.mapToResource(
            finisher: T.() -> R
    ): Single<Resource<R>> = map(resourceMapper(finisher))

    protected fun <T : Any, E : Any, R> resourceMapper(
            finisher: T.() -> R
    ): (NetworkResponse<T, E>) -> Resource<R> = { response ->
        when (response) {
            is NetworkResponse.Success -> Resource.Success(response.body.finisher())
            is NetworkResponse.ServerError -> Resource.Error(response.body)
            is NetworkResponse.NetworkError -> Resource.Error(response.error)
        }
    }

    protected fun <T : Any, E : Any, R> throwingResourceMapper(
            finisher: T.() -> R
    ): (NetworkResponse<T, E>) -> R = { response ->
        when (response) {
            is NetworkResponse.Success -> Resource.Success(response.body.finisher()).data
            is NetworkResponse.NetworkError -> throw response.error
            is NetworkResponse.ServerError -> throw ThrowableServerError(response)
        }
    }

    protected fun <T : Any, E : Any, R> Observable<NetworkResponse<T, E>>.mapToDataOrThrow(
            finisher: T.() -> R
    ): Observable<R> = map(throwingResourceMapper(finisher))

    protected fun <T : Any, E : Any, R> Single<NetworkResponse<T, E>>.mapToDataOrThrow(
            finisher: T.() -> R
    ): Single<R> = map(throwingResourceMapper(finisher))
}