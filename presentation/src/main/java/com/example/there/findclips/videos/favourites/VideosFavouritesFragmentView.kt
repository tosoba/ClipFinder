package com.example.there.findclips.videos.favourites

import android.databinding.ObservableList
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