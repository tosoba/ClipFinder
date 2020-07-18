package com.example.there.domain.entity

class Page<T>(val items: List<T>, val offset: Int, val total: Int) {
    fun <S> map(mapper: (T) -> S): Page<S> = Page(items.map(mapper), offset, total)
}
