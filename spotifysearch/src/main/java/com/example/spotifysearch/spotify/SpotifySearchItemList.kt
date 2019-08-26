package com.example.spotifysearch.spotify

import com.example.coreandroid.model.DataStatus
import com.example.coreandroid.model.HoldsData
import com.example.coreandroid.model.Initial

data class SpotifySearchItemList<I>(
        override val value: Collection<I> = emptyList(), override val status: DataStatus = Initial
) : HoldsData<Collection<I>> {
    override val copyWithLoadingInProgress: HoldsData<Collection<I>> get() = throw UnsupportedOperationException()
    override fun <E> copyWithError(error: E): HoldsData<Collection<I>> = throw UnsupportedOperationException()
    operator fun plus(other: Collection<I>) = copy(value = this.value + other)
}