package com.example.there.findclips.activities.playlist

import android.databinding.ObservableField
import android.view.View
import com.example.there.findclips.model.entities.Playlist

data class PlaylistView(
        val state: PlaylistViewState,
        val playlist: Playlist,
        val onFavouriteBtnClickListener: View.OnClickListener
)

data class PlaylistViewState(
        val loadingInProgress: ObservableField<Boolean> = ObservableField(false)
)