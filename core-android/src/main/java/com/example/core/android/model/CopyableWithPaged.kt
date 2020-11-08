package com.example.core.android.model

import com.example.core.model.Paged

interface CopyableWithPaged<I, T : CopyableWithPaged<I, T>> {
    fun copyWithPaged(paged: Paged<Iterable<I>>): T
}
