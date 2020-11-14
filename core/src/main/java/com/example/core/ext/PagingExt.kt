package com.example.core.ext

import com.example.core.SpotifyDefaults
import com.example.core.model.IPagingObject
import com.example.core.model.Paged

fun <T : Any> IPagingObject<T>.toPaged(
    extraOffset: Int = SpotifyDefaults.LIMIT
): Paged<List<T>> = Paged(items, offset + extraOffset, total)

fun <T : Any, R> IPagingObject<T>.toPaged(
    extraOffset: Int = SpotifyDefaults.LIMIT,
    mapItems: (T) -> R
): Paged<List<R>> = Paged(items.map(mapItems), offset + extraOffset, total)
