package com.clipfinder.spotify.category

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.Empty
import com.example.core.android.model.Loadable
import com.example.core.android.model.PagedItemsList
import com.example.core.android.spotify.model.Category
import com.example.core.android.spotify.model.Playlist

data class SpotifyCategoryState(
    val category: Category,
    val playlists: Loadable<PagedItemsList<Playlist>>
) : MvRxState {
    constructor(category: Category) : this(category, Empty)
}
