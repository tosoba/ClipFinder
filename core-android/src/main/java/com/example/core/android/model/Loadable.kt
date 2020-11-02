package com.example.core.android.model

interface HasValue<out T> {
    val value: T
}

sealed class Loadable<out T>

object Empty : Loadable<Nothing>()

data class Success<out T>(override val value: T) : Loadable<T>(), HasValue<T>

sealed class Failure<out T> : Loadable<T>() {
    abstract val error: Throwable

    data class WithValue<out T>(override val value: T, override val error: Throwable) : Failure<T>(), HasValue<T>
    data class NoValue<out T>(override val error: Throwable) : Failure<T>()
}
