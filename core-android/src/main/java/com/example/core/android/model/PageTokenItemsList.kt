package com.example.core.android.model

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
