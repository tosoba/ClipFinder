package com.example.there.findclips.videos.favourites

import android.databinding.ObservableList
import com.example.there.findclips.util.list.ObservableSortedList
import com.example.there.findclips.view.list.item.RecyclerViewItemView
import com.example.there.findclips.view.viewflipper.PlaylistThumbnailView

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