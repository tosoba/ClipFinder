package com.example.core.android.model

import com.clipfinder.core.model.Paged

interface CopyableWithPaged<I, T : CopyableWithPaged<I, T>> {
    fun copyWithPaged(paged: Paged<Iterable<I>>): T
}
