package com.example.core.android.util.ext

import com.example.core.android.model.*
import com.example.core.model.Paged
import java.io.IOException

val <L : BaseLoadable<T, L>, T> L.retryLoadOnNetworkAvailable: Boolean
    get() = this is HasError && (error == null || error is IOException)

val <T : ItemsList<I>, I> DefaultLoadable<T>.retryLoadItemsOnNetworkAvailable: Boolean
    get() = value.items.isEmpty() && this is HasError && (error == null || error is IOException)

fun <T : CopyableWithPaged<I, T>, I, IT : Iterable<I>> DefaultLoadable<T>.copyWithPaged(
    paged: Paged<IT>
): DefaultLoadable<T> = DefaultReady(value.copyWithPaged(paged))
