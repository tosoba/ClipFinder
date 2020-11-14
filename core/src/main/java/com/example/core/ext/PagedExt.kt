package com.example.core.ext

import com.example.core.model.Paged

fun <T : Iterable<I>, S, I> Paged<T>.map(mapper: (I) -> S): Paged<List<S>> = Paged(
    contents.map(mapper),
    offset,
    total
)

fun <T : Iterable<I>, S, I> Paged<T>.mapIndexed(mapper: (Int, I) -> S): Paged<List<S>> = Paged(
    contents.mapIndexed(mapper),
    offset,
    total
)
