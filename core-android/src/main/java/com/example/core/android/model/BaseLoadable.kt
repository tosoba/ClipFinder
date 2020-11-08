package com.example.core.android.model

interface BaseLoadable<out T, out L : BaseLoadable<T, L>> {
    val copyWithLoadingInProgress: L
    val copyWithClearedError: L
    fun copyWithError(error: Any?): L
}