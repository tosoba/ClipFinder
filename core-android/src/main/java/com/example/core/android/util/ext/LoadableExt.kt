package com.example.core.android.util.ext

import com.clipfinder.core.model.*
import java.io.IOException

val <T> Loadable<T>.retryLoadOnNetworkAvailable: Boolean
    get() = this is Failed && (error == null || error is IOException)

val <T : Collection<I>, I> Loadable<T>.retryLoadCollectionOnConnected: Boolean
    get() = (if (this is WithValue) value.isEmpty() else true)
        && this is Failed
        && (error == null || error is IOException)

val <T : PagedList<I>, I> Loadable<T>.offset: Int
    get() = if (this is WithValue) value.offset else 0

fun <T : CopyableWithPaged<I, T>, I, IT : Iterable<I>> WithValue<T>.copyWithPaged(
    paged: Paged<IT>
): Loadable<T> = Ready(value.copyWithPaged(paged))

val <T : CompletionTrackable> Loadable<T>.completed: Boolean
    get() = this is WithValue && value.completed

val <T : CompletionTrackable> Loadable<T>.loadingOrCompleted: Boolean
    get() = completed || this is LoadingInProgress
