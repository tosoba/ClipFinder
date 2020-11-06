package com.example.core.android.model

import com.example.core.ext.castAs
import com.example.core.model.Paged

interface HasValue<T> {
    val value: T
}

sealed class Loadable<out T> {
    open val copyWithLoadingInProgress: LoadingInProgress<T>
        get() = LoadingInProgress.WithoutValue

    open fun copyWithError(error: Any): Failed<T> = Failed.WithoutValue(error)
}

object Empty : Loadable<Nothing>()

sealed class LoadingInProgress<out T> : Loadable<T>() { //TODO: rename
    object WithoutValue : LoadingInProgress<Nothing>()

    data class WithValue<T>(override val value: T) : LoadingInProgress<T>(), HasValue<T> {
        override val copyWithLoadingInProgress: WithValue<T>
            get() = this

        override fun copyWithError(error: Any): Failed<T> = Failed.WithValue(value, error)
    }
}

data class Ready<T>(override val value: T) : Loadable<T>(), HasValue<T> {
    override val copyWithLoadingInProgress: LoadingInProgress.WithValue<T>
        get() = LoadingInProgress.WithValue(value)

    override fun copyWithError(error: Any): Failed<T> = Failed.WithValue(value, error)
}

sealed class Failed<out T> : Loadable<T>() {
    abstract val error: Any

    data class WithValue<T>(
        override val value: T,
        override val error: Any
    ) : Failed<T>(),
        HasValue<T> {

        val copyWithClearedError: Ready<T>
            get() = Ready(value)

        override val copyWithLoadingInProgress: LoadingInProgress.WithValue<T>
            get() = LoadingInProgress.WithValue(value)

        override fun copyWithError(error: Any): Failed<T> = WithValue(value, error)
    }

    data class WithoutValue(override val error: Any) : Failed<Nothing>() {
        override val copyWithLoadingInProgress: LoadingInProgress.WithoutValue
            get() = LoadingInProgress.WithoutValue
    }
}

interface CopyableWithPaged<I, T : CopyableWithPaged<I, T>> {
    fun copyWithPaged(paged: Paged<Iterable<I>>): T
}

fun <T : CopyableWithPaged<I, T>, I> Loadable<T>.copyWithPaged(
    paged: Paged<Iterable<I>>
): Loadable<T> = castAs<HasValue<T>>()
    ?.let { Ready(it.value.copyWithPaged(paged)) }
    ?: throw IllegalArgumentException()

interface CompletionTrackable {
    val completed: Boolean
}

data class PagedItemsList<I>(
    val items: List<I> = emptyList(),
    val offset: Int = 0,
    val total: Int = Integer.MAX_VALUE
) : CopyableWithPaged<I, PagedItemsList<I>>,
    CompletionTrackable {

    override val completed: Boolean
        get() = offset < total

    override fun copyWithPaged(paged: Paged<Iterable<I>>): PagedItemsList<I> = copy(
        items = items + paged.contents,
        offset = paged.offset,
        total = paged.total
    )
}

data class PageTokenItemsList<I>(
    val items: List<I> = emptyList(),
    val nextPageToken: String? = null
) : CompletionTrackable {

    override val completed: Boolean
        get() = nextPageToken != null || items.isEmpty()

    fun copyWith(newItems: Iterable<I>, nextPageToken: String?): PageTokenItemsList<I> = copy(
        items = items + newItems,
        nextPageToken = nextPageToken
    )
}

