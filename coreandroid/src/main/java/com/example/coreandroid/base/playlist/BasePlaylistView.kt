package com.example.coreandroid.base.playlist

import android.view.View
import androidx.databinding.ObservableField

class PlaylistView<Playlist>(
        val state: PlaylistViewState,
        val playlist: Playlist,
        val onFavouriteBtnClickListener: View.OnClickListener
)

class PlaylistViewState(
        val loadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val isSavedAsFavourite: ObservableField<Boolean> = ObservableField(false)
)