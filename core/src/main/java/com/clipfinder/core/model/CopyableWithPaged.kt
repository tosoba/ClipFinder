package com.clipfinder.core.model

interface CopyableWithPaged<I, T : CopyableWithPaged<I, T>> {
    fun copyWithPaged(paged: Paged<Iterable<I>>): T
}
