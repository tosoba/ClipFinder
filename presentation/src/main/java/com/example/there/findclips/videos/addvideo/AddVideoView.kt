package com.example.there.findclips.videos.addvideo

import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.view.View
import com.example.there.findclips.model.entity.videos.VideoPlaylist
import com.example.there.findclips.view.list.item.RecyclerViewItemView

class AddVideoView(
        val state: AddVideoViewState,
        val playlistsRecyclerViewItemView: RecyclerViewItemView<VideoPlaylist>,
        val onAddNewPlaylistBtnClickListener: View.OnClickListener
)

class AddVideoViewState(val playlists: ObservableList<VideoPlaylist> = ObservableArrayList())