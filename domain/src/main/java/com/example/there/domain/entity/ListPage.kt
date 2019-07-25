package com.example.there.domain.entity

class ListPage<T>(
        val items: List<T>,
        val offset: Int,
        val totalItems: Int
) {
    fun <S> map(mapper: (T) -> S): ListPage<S> = ListPage(items.map(mapper), offset, totalItems)
}