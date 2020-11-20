package com.example.core.android.util.ext

import com.example.core.android.model.*
import com.example.core.ext.castAs
import com.example.core.model.Paged
import java.io.IOException

val <L : BaseLoadable<T, L>, T> L.retryLoadOnNetworkAvailable: Boolean
    get() = this is HasError && (error == null || error is IOException)

val <T : Collection<I>, I> Loadable<T>.retryLoadItemsOnNetworkAvailable2: Boolean
    get() = castAs<HasValue<T>>()?.value?.isEmpty() ?: true
        && this is HasError
        && (error == null || error is IOException)

val <T : PagedItemsList<I>, I> Loadable<T>.offset: Int
    get() = castAs<HasValue<T>>()?.value?.offset ?: 0

val <T : Collection<I>, I> DefaultLoadable<T>.retryLoadItemsOnNetworkAvailable: Boolean
    get() = value.isEmpty() && this is HasError && (error == null || error is IOException)

fun <T : CopyableWithPaged<I, T>, I, IT : Iterable<I>> DefaultLoadable<T>.copyWithPaged(
    paged: Paged<IT>
): DefaultLoadable<T> = DefaultReady(value.copyWithPaged(paged))

fun <T : CopyableWithPaged<I, T>, I, IT : Iterable<I>> HasValue<T>.copyWithPaged(
    paged: Paged<IT>
): Loadable<T> = Ready(value.copyWithPaged(paged))
