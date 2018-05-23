package com.example.there.findclips.fragments.favourites.videos

import android.databinding.ObservableArrayList
import android.support.v7.widget.RecyclerView
import com.example.there.findclips.model.entities.VideoPlaylist
import com.example.there.findclips.view.lists.VideoPlaylistsList

data class VideosFavouritesFragmentView(
        val state: VideosFavouritesFragmentViewState,
        val playlistsAdapter: VideoPlaylistsList.Adapter,
        val itemDecoration: RecyclerView.ItemDecoration
)

data class VideosFavouritesFragmentViewState(
        val playlists: ObservableArrayList<VideoPlaylist> = ObservableArrayList()
)