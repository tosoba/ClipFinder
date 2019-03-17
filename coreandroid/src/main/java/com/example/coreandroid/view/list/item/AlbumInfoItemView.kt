package com.example.coreandroid.view.list.item

import android.databinding.ObservableField
import android.view.View

class AlbumInfoItemView(
        val state: AlbumInfoViewState,
        val onClickListener: View.OnClickListener
)

class AlbumInfoViewState(
        val albumLoadingInProgress: ObservableField<Boolean>,
        val album: ObservableField<Album>
)