package com.example.core.android.model

interface HasValue<out T> {
    val value: T
}

sealed class Loadable<out T> {
    open val copyWithLoadingInProgress: LoadingInProgress<T>
        get() = LoadingInProgress.WithoutValue

    open fun copyWithError(error: Any?): Failed<T> = Failed.WithoutValue(error)

    open val copyWithClearedError: Loadable<T>
        get() = this
}

object Empty : Loadable<Nothing>()

sealed class LoadingInProgress<out T> : Loadable<T>() { //TODO: rename
    object WithoutValue : LoadingInProgress<Nothing>()

    data class WithValue<T>(override val value: T) : LoadingInProgress<T>(), HasValue<T> {
        override val copyWithLoadingInProgress: WithValue<T>
            get() = this

        override fun copyWithError(error: Any?): Failed<T> = Failed.WithValue(value, error)
    }
}

data class Ready<T>(override val value: T) : Loadable<T>(), HasValue<T> {
    override val copyWithLoadingInProgress: LoadingInProgress.WithValue<T>
        get() = LoadingInProgress.WithValue(value)

    override fun copyWithError(error: Any?): Failed<T> = Failed.WithValue(value, error)
}

sealed class Failed<out T> : Loadable<T>() {
    abstract val error: Any?

    data class WithValue<T>(
        override val value: T,
        override val error: Any?
    ) : Failed<T>(),
        HasValue<T> {

        override val copyWithClearedError: Ready<T>
            get() = Ready(value)

        override val copyWithLoadingInProgress: LoadingInProgress.WithValue<T>
            get() = LoadingInProgress.WithValue(value)

        override fun copyWithError(error: Any?): Failed<T> = WithValue(value, error)
    }

    data class WithoutValue(override val error: Any?) : Failed<Nothing>() {
        override val copyWithLoadingInProgress: LoadingInProgress.WithoutValue
            get() = LoadingInProgress.WithoutValue

        override val copyWithClearedError: Loadable<Nothing>
            get() = Empty
    }
}
