package com.example.coreandroid.base.playlist

import android.os.Parcelable
import android.view.View
import com.airbnb.mvrx.MvRxState
import com.example.coreandroid.model.Data
import com.example.coreandroid.model.PagedDataList

class PlaylistView<Playlist>(
    val playlist: Playlist,
    val onFavouriteBtnClickListener: View.OnClickListener
)

data class PlaylistViewState<Playlist : Parcelable, Track>(
    val playlist: Playlist,
    val tracks: PagedDataList<Track> = PagedDataList(),
    val isSavedAsFavourite: Data<Boolean> = Data(false)
) : MvRxState {
    constructor(playlist: Playlist) : this(playlist, PagedDataList(), Data(false))
}
