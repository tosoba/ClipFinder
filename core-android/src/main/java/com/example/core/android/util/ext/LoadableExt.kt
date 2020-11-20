package com.example.core.android.util.ext

import com.example.core.android.model.*
import com.example.core.model.Paged
import java.io.IOException

val <L : BaseLoadable<T, L>, T> L.retryLoadOnNetworkAvailable: Boolean
    get() = this is Failed && (error == null || error is IOException)

val <T : Collection<I>, I> Loadable<T>.retryLoadCollectionOnConnected: Boolean
    get() = (if (this is WithValue) value.isEmpty() else true)
        && this is Failed
        && (error == null || error is IOException)

val <T : PagedItemsList<I>, I> Loadable<T>.offset: Int
    get() = if (this is WithValue) value.offset else 0

val <T : Collection<I>, I> DefaultLoadable<T>.retryLoadItemsOnNetworkAvailable: Boolean
    get() = value.isEmpty() && this is Failed && (error == null || error is IOException)

fun <T : CopyableWithPaged<I, T>, I, IT : Iterable<I>> DefaultLoadable<T>.copyWithPaged(
    paged: Paged<IT>
): DefaultLoadable<T> = DefaultReady(value.copyWithPaged(paged))

fun <T : CopyableWithPaged<I, T>, I, IT : Iterable<I>> WithValue<T>.copyWithPaged(
    paged: Paged<IT>
): Loadable<T> = Ready(value.copyWithPaged(paged))

val <T : CompletionTrackable> Loadable<T>.isLoadingOrCompleted: Boolean
    get() = (this is WithValue && value.completed) || this is LoadingInProgress
