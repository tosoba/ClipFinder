package com.example.spotifycategory

import android.view.View
import com.airbnb.mvrx.MvRxState
import com.example.coreandroid.model.Data
import com.example.coreandroid.model.PagedDataList
import com.example.coreandroid.model.spotify.Category
import com.example.coreandroid.model.spotify.Playlist

class CategoryView(
        val category: Category,
        val onFavouriteBtnClickListener: View.OnClickListener
)

data class CategoryViewState(
        val category: Category,
        val isSavedAsFavourite: Data<Boolean> = Data(false),
        val playlists: PagedDataList<Playlist> = PagedDataList()
) : MvRxState {
    constructor(category: Category) : this(category, Data(false), PagedDataList())
}