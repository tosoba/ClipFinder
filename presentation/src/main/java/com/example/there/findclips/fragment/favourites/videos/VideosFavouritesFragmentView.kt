package com.example.there.findclips.fragment.favourites.videos

import android.databinding.ObservableList
import android.support.v7.widget.RecyclerView
import com.example.there.findclips.model.entity.VideoPlaylist
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.view.list.impl.VideoPlaylistsList

class VideosFavouritesFragmentView(
        val state: VideosFavouritesFragmentViewState,
        val playlistsAdapter: VideoPlaylistsList.Adapter,
        val itemDecoration: RecyclerView.ItemDecoration
)

data class VideosFavouritesFragmentViewState(
        val playlists: ObservableList<VideoPlaylist> = ObservableSortedList<VideoPlaylist>(
                VideoPlaylist::class.java,
                VideoPlaylist.sortedListCallback
        )
)