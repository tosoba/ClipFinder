package com.example.core.model

data class StringIdModel(val id: String)

data class StringUrlModel(val url: String)

data class Paged<T>(val data: T, val offset: Int, val total: Int)

fun <S, I> Paged<Iterable<I>>.map(mapper: (I) -> S): Paged<List<S>> = Paged(data.map(mapper), offset, total)
