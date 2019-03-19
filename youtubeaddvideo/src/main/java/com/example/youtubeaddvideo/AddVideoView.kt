package com.example.youtubeaddvideo

import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.view.View
import com.example.coreandroid.model.videos.VideoPlaylist
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemView

class AddVideoView(
        val state: AddVideoViewState,
        val playlistsRecyclerViewItemView: RecyclerViewItemView<VideoPlaylist>,
        val onAddNewPlaylistBtnClickListener: View.OnClickListener
)

class AddVideoViewState(val playlists: ObservableList<VideoPlaylist> = ObservableArrayList())