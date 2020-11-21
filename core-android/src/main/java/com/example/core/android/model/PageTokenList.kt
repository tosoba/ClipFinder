package com.example.core.android.model

data class PageTokenList<I>(
    val items: List<I> = emptyList(),
    val nextPageToken: String? = null
) : Collection<I> by items,
    CompletionTrackable {

    override val completed: Boolean
        get() = !(nextPageToken != null || items.isEmpty())

    fun copyWith(newItems: Iterable<I>, nextPageToken: String?): PageTokenList<I> = copy(
        items = items + newItems,
        nextPageToken = nextPageToken
    )
}
