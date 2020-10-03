package com.example.core.model

data class StringIdModel(val id: String)

data class StringUrlModel(val url: String)

data class Paged<T>(val contents: T, val offset: Int, val total: Int)

fun <T : Iterable<I>, S, I> Paged<T>.map(
    mapper: (I) -> S
): Paged<List<S>> = Paged(contents.map(mapper), offset, total)
