package com.clipfinder.spotify.category

import com.airbnb.mvrx.MvRxState
import com.clipfinder.core.android.spotify.model.Category
import com.clipfinder.core.android.spotify.model.Playlist
import com.clipfinder.core.model.Empty
import com.clipfinder.core.model.Loadable
import com.clipfinder.core.model.PagedList

data class SpotifyCategoryState(
    val category: Category,
    val playlists: Loadable<PagedList<Playlist>>
) : MvRxState {
    constructor(category: Category) : this(category, Empty)
}
