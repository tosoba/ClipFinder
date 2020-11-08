package com.example.core.android.model

import com.example.core.model.Paged

interface CopyableWithPaged<I, T : CopyableWithPaged<I, T>> {
    fun copyWithPaged(paged: Paged<Iterable<I>>): T
}

fun <T : CopyableWithPaged<I, T>, I, IT : Iterable<I>> DefaultLoadable<T>.copyWithPaged(
    paged: Paged<IT>
): DefaultLoadable<T> = DefaultReady(value.copyWithPaged(paged))
