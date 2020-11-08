package com.example.core.android.model

sealed class DefaultLoadable<out T> : HasValue<T>, BaseLoadable<T, DefaultLoadable<T>> {
    override val copyWithLoadingInProgress: DefaultLoadable<T>
        get() = if (this is DefaultInProgress) this else DefaultInProgress(value)

    override val copyWithClearedError: DefaultLoadable<T>
        get() = if (this is DefaultFailed) DefaultReady(value) else this

    override fun copyWithError(error: Any?): DefaultLoadable<T> = DefaultFailed(value, error)
}

data class DefaultInProgress<out T>(override val value: T) : DefaultLoadable<T>(), HasValue<T>

data class DefaultReady<out T>(override val value: T) : DefaultLoadable<T>(), HasValue<T>

data class DefaultFailed<out T>(
    override val value: T,
    override val error: Any?
) : DefaultLoadable<T>(),
    HasError
