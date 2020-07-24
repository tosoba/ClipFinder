package com.example.youtubefavourites

import androidx.databinding.ObservableList
import com.example.core.android.util.list.ObservableSortedList
import com.example.core.android.view.recyclerview.item.RecyclerViewItemView
import com.example.core.android.view.viewflipper.PlaylistThumbnailView

class VideosFavouritesFragmentView(
    val state: VideosFavouritesFragmentViewState,
    val playlistsRecyclerViewItemView: RecyclerViewItemView<PlaylistThumbnailView>
)

class VideosFavouritesFragmentViewState(
    val playlists: ObservableList<PlaylistThumbnailView> = ObservableSortedList(
        PlaylistThumbnailView::class.java,
        PlaylistThumbnailView.sortedListCallback
    )
)