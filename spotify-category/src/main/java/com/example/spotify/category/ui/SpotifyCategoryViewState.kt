package com.example.spotify.category.ui

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.PagedDataList
import com.example.core.android.spotify.model.Playlist
import com.example.core.android.spotify.model.Category

data class SpotifyCategoryViewState(
    val category: Category,
    val playlists: PagedDataList<Playlist> = PagedDataList()
) : MvRxState {
    constructor(category: Category) : this(category, PagedDataList())
}
