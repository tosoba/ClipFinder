package com.example.soundcloudplaylist

import android.databinding.ObservableField
import android.view.View
import com.example.coreandroid.model.soundcloud.ISoundCloudPlaylist

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