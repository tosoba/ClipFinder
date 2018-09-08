package com.example.there.findclips.fragment.favourites.videos

import android.databinding.ObservableArrayList
import android.support.v7.widget.RecyclerView
import com.example.there.findclips.model.entity.VideoPlaylist
import com.example.there.findclips.view.list.VideoPlaylistsList

data class VideosFavouritesFragmentView(
        val state: VideosFavouritesFragmentViewState,
        val playlistsAdapter: VideoPlaylistsList.Adapter,
        val itemDecoration: RecyclerView.ItemDecoration
)

data class VideosFavouritesFragmentViewState(
        val playlists: ObservableArrayList<VideoPlaylist> = ObservableArrayList()
)