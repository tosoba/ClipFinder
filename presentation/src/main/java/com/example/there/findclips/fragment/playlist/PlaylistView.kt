package com.example.there.findclips.fragment.playlist

import android.databinding.ObservableField
import android.view.View
import com.example.there.findclips.model.entity.Playlist

class PlaylistView(
        val state: PlaylistViewState,
        val playlist: Playlist,
        val onFavouriteBtnClickListener: View.OnClickListener
)

data class PlaylistViewState(
        val loadingInProgress: ObservableField<Boolean> = ObservableField(false)
)