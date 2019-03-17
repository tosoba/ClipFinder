package com.example.there.findclips.spotify.spotifyitem.playlist

import android.databinding.ObservableField
import android.view.View

class PlaylistView(
        val state: PlaylistViewState,
        val playlist: Playlist,
        val onFavouriteBtnClickListener: View.OnClickListener,
        val onPlayBtnClickListener: View.OnClickListener
)

class PlaylistViewState(
        val loadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val isSavedAsFavourite: ObservableField<Boolean> = ObservableField(false)
)