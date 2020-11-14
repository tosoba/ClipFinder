package com.example.core.model

import java.io.IOException

sealed class NetworkResponse<out T : Any, out E : Any> {
    data class Success<T : Any>(val body: T) : NetworkResponse<T, Nothing>()
    data class ServerError<E : Any>(val body: E?, val code: Int?) : NetworkResponse<Nothing, E>()
    data class NetworkError(val error: IOException) : NetworkResponse<Nothing, Nothing>()
    data class DifferentError(val error: Throwable) : NetworkResponse<Nothing, Nothing>()
}
