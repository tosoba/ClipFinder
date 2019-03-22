package com.example.coreandroid.view.recyclerview.item

import android.databinding.ObservableField
import android.view.View
import com.example.coreandroid.model.spotify.Album

class AlbumInfoItemView(
        val state: AlbumInfoViewState,
        val onClickListener: View.OnClickListener
)

class AlbumInfoViewState(
        val albumLoadingInProgress: ObservableField<Boolean>,
        val album: ObservableField<Album>
)