package com.example.youtubeaddvideo

import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import com.example.core.android.model.videos.VideoPlaylist
import com.example.core.android.view.recyclerview.item.RecyclerViewItemView

class AddVideoView(
    val state: AddVideoViewState,
    val playlistsRecyclerViewItemView: RecyclerViewItemView<VideoPlaylist>,
    val onAddNewPlaylistBtnClickListener: View.OnClickListener
)

class AddVideoViewState(val playlists: ObservableList<VideoPlaylist> = ObservableArrayList())
