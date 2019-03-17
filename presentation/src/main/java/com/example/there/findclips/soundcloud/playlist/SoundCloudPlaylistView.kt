package com.example.there.findclips.soundcloud.playlist

import android.databinding.ObservableField
import android.view.View

class SoundCloudPlaylistView(
        val state: SoundCloudPlaylistViewState,
        val playlist: ISoundCloudPlaylist,
        val onFavouriteBtnClickListener: View.OnClickListener,
        val onPlayBtnClickListener: View.OnClickListener
)

class SoundCloudPlaylistViewState(
        val loadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val isSavedAsFavourite: ObservableField<Boolean> = ObservableField(false)
)