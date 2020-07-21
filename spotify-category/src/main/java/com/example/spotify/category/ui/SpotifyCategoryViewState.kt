package com.example.spotify.category.ui

import com.airbnb.mvrx.MvRxState
import com.example.coreandroid.model.Data
import com.example.coreandroid.model.PagedDataList
import com.example.coreandroid.model.spotify.Category
import com.example.coreandroid.model.spotify.Playlist

data class SpotifyCategoryViewState(
    val category: Category,
    val isSavedAsFavourite: Data<Boolean> = Data(false),
    val playlists: PagedDataList<Playlist> = PagedDataList()
) : MvRxState {
    constructor(category: Category) : this(category, Data(false), PagedDataList())
}
