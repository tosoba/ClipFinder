package com.example.core.android.model

data class PageTokenItemsList<I>(
    val items: List<I> = emptyList(),
    val nextPageToken: String? = null
) : CompletionTrackable {

    override val completed: Boolean
        get() = !(nextPageToken != null || items.isEmpty())

    fun copyWith(newItems: Iterable<I>, nextPageToken: String?): PageTokenItemsList<I> = copy(
        items = items + newItems,
        nextPageToken = nextPageToken
    )
}