package com.clipfinder.spotify.category

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.DefaultLoadable
import com.example.core.android.model.DefaultReady
import com.example.core.android.model.PagedDataList
import com.example.core.android.model.PagedItemsList
import com.example.core.android.spotify.model.Playlist
import com.example.core.android.spotify.model.Category

data class SpotifyCategoryState(
    val category: Category,
    val playlists: DefaultLoadable<PagedItemsList<Playlist>> = DefaultReady(PagedItemsList())
) : MvRxState {
    constructor(category: Category) : this(category, DefaultReady(PagedItemsList()))
}
