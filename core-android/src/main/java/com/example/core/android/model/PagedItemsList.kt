package com.example.core.android.model

import com.example.core.model.Paged

data class PagedItemsList<I>(
    val items: List<I> = emptyList(),
    val offset: Int = 0,
    val total: Int = Integer.MAX_VALUE
) : Collection<I> by items,
    CompletionTrackable,
    CopyableWithPaged<I, PagedItemsList<I>> {

    constructor(paged: Paged<List<I>>) : this(
        items = paged.contents,
        offset = paged.offset,
        total = paged.total
    )

    override val completed: Boolean
        get() = offset >= total

    override fun copyWithPaged(paged: Paged<Iterable<I>>): PagedItemsList<I> = copy(
        items = items + paged.contents,
        offset = paged.offset,
        total = paged.total
    )
}