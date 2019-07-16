package com.example.youtubefavourites

import androidx.databinding.ObservableList
import com.example.coreandroid.util.list.ObservableSortedList
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemView
import com.example.coreandroid.view.viewflipper.PlaylistThumbnailView

class VideosFavouritesFragmentView(
        val state: VideosFavouritesFragmentViewState,
        val playlistsRecyclerViewItemView: RecyclerViewItemView<PlaylistThumbnailView>
)

class VideosFavouritesFragmentViewState(
        val playlists: ObservableList<PlaylistThumbnailView> = ObservableSortedList<PlaylistThumbnailView>(
                PlaylistThumbnailView::class.java,
                PlaylistThumbnailView.sortedListCallback
        )
)