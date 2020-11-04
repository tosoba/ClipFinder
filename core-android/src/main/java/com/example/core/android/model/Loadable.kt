package com.example.core.android.model

interface HasValue<out T> {
    val value: T
}

sealed class Loadable<out T> {
    open val copyWithLoadingInProgress: LoadingInProgress<T>
        get() = LoadingInProgress.WithoutValue
}

object Empty : Loadable<Nothing>()

sealed class LoadingInProgress<out T> : Loadable<T>() { //TODO: rename
    object WithoutValue : LoadingInProgress<Nothing>()

    data class WithValue<out T>(override val value: T) : LoadingInProgress<T>(), HasValue<T> {
        override val copyWithLoadingInProgress: WithValue<T>
            get() = this
    }
}

data class Loaded<out T>(override val value: T) : Loadable<T>(), HasValue<T> {
    override val copyWithLoadingInProgress: LoadingInProgress.WithValue<T>
        get() = LoadingInProgress.WithValue(value)
}

sealed class Failed<out T> : Loadable<T>() {
    abstract val error: Throwable

    data class WithValue<out T>(
        override val value: T,
        override val error: Throwable
    ) : Failed<T>(),
        HasValue<T> {

        val copyWithClearedError: Loaded<T>
            get() = Loaded(value)

        override val copyWithLoadingInProgress: LoadingInProgress.WithValue<T>
            get() = LoadingInProgress.WithValue(value)
    }

    data class WithoutValue(override val error: Throwable) : Failed<Nothing>() {
        override val copyWithLoadingInProgress: LoadingInProgress.WithoutValue
            get() = LoadingInProgress.WithoutValue
    }
}

interface CopyableWithItems<I, T : CopyableWithItems<I, T>> {
    fun copyWithItems(newItems: Iterable<I>): T
    fun copyWithItems(vararg newItems: I): T
}

interface CompletionTrackable {
    val completed: Boolean
}

data class PagedItemsList<I>(
    val items: List<I>,
    val offset: Int = 0,
    val total: Int = Integer.MAX_VALUE
) : CopyableWithItems<I, PagedItemsList<I>>,
    CompletionTrackable {

    override val completed: Boolean
        get() = offset < total

    override fun copyWithItems(newItems: Iterable<I>): PagedItemsList<I> = copy(
        items = items + newItems,
        offset = offset,
        total = total
    )

    override fun copyWithItems(vararg newItems: I): PagedItemsList<I> = copy(
        items = items + newItems,
        offset = offset,
        total = total
    )
}

data class PageTokenItemsList<I>(
    val items: List<I>,
    val nextPageToken: String? = null
) : CompletionTrackable {

    override val completed: Boolean
        get() = nextPageToken != null || items.isEmpty()

    fun copyWith(newItems: Iterable<I>, nextPageToken: String?): PageTokenItemsList<I> = copy(
        items = items + newItems,
        nextPageToken = nextPageToken
    )
}
