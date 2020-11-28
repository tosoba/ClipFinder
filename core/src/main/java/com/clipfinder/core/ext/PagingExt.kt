package com.clipfinder.core.ext

import com.clipfinder.core.SpotifyDefaults
import com.clipfinder.core.model.IPagingObject
import com.clipfinder.core.model.Paged

fun <T : Any> IPagingObject<T>.toPaged(
    extraOffset: Int = SpotifyDefaults.LIMIT
): Paged<List<T>> = Paged(items, offset + extraOffset, total)

fun <T : Any, R> IPagingObject<T>.toPaged(
    extraOffset: Int = SpotifyDefaults.LIMIT,
    mapItems: (T) -> R
): Paged<List<R>> = Paged(items.map(mapItems), offset + extraOffset, total)
