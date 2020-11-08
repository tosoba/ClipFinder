package com.example.core.android.model

import java.io.IOException

data class PageTokenItemsList<I>(
    override val items: List<I> = emptyList(),
    val nextPageToken: String? = null
) : ItemsList<I>,
    CompletionTrackable {

    override val completed: Boolean
        get() = !(nextPageToken != null || items.isEmpty())

    fun copyWith(newItems: Iterable<I>, nextPageToken: String?): PageTokenItemsList<I> = copy(
        items = items + newItems,
        nextPageToken = nextPageToken
    )
}

val <L : BaseLoadable<T, L>, T> L.retryLoadOnNetworkAvailable: Boolean
    get() = this is HasError && (error == null || error is IOException)


val <T : ItemsList<I>, I> DefaultLoadable<T>.retryLoadItemsOnNetworkAvailable: Boolean
    get() = value.items.isEmpty() && this is HasError && (error == null || error is IOException)
