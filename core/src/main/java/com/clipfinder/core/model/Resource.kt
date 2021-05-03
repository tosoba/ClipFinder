package com.clipfinder.core.model

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error<out T>(val error: Any?, val data: T? = null) : Resource<T>()

    fun <S> map(mapper: (T) -> S): Resource<S> =
        when (this) {
            is Success<T> -> Success(mapper(data))
            is Error<T> -> Error(error, data?.let(mapper))
        }

    companion object {
        fun <T> success(data: T): Resource<T> = Success(data)
        fun <T> error(error: Any, data: T? = null): Resource<T> = Error(error, data)
    }
}
