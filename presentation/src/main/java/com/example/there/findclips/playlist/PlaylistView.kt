package com.example.there.findclips.playlist

import android.view.View
import com.example.there.findclips.entities.Playlist

data class PlaylistView(
        val state: PlaylistViewState,
        val playlist: Playlist,
        val onFavouriteBtnClickListener: View.OnClickListener
)