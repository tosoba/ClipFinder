package com.example.core.android.model

sealed class DefaultLoadable<T> : HasValue<T> {
    val copyWithLoadingInProgress: DefaultLoadable<T>
        get() = if (this is DefaultInProgress) this else DefaultInProgress(value)

    val copyWithClearedError: DefaultLoadable<T>
        get() = if (this is DefaultFailed) DefaultReady(value) else this

    fun copyWithError(error: Any?): DefaultLoadable<T> = DefaultFailed(value, error)
}

data class DefaultInProgress<T>(override val value: T) : DefaultLoadable<T>(), HasValue<T>

data class DefaultReady<T>(override val value: T) : DefaultLoadable<T>(), HasValue<T>

data class DefaultFailed<T>(override val value: T, val error: Any?) : DefaultLoadable<T>()
