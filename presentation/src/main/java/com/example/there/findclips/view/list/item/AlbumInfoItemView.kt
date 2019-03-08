package com.example.there.findclips.view.list.item

import android.databinding.ObservableField
import android.view.View
import com.example.there.findclips.model.entity.spotify.Album

class AlbumInfoItemView(
        val state: AlbumInfoViewState,
        val onClickListener: View.OnClickListener
)

class AlbumInfoViewState(
        val albumLoadingInProgress: ObservableField<Boolean>,
        val album: ObservableField<Album>
)