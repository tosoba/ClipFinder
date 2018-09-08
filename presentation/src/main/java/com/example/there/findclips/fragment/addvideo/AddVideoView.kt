package com.example.there.findclips.fragment.addvideo

import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.there.findclips.model.entity.VideoPlaylist
import com.example.there.findclips.view.list.VideoPlaylistsList

data class AddVideoView(
        val state: AddVideoViewState,
        val playlistsAdapter: VideoPlaylistsList.Adapter,
        val itemDecoration: RecyclerView.ItemDecoration,
        val onAddNewPlaylistBtnClickListener: View.OnClickListener
)

data class AddVideoViewState(val playlists: ObservableList<VideoPlaylist> = ObservableArrayList())