package com.example.there.findclips.fragment.favourites.videos

import android.databinding.ObservableList
import com.example.there.findclips.model.entity.VideoPlaylistWithThumbnails
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.view.list.item.RecyclerViewItemView

class VideosFavouritesFragmentView(
        val state: VideosFavouritesFragmentViewState,
        val playlistsRecyclerViewItemView: RecyclerViewItemView<VideoPlaylistWithThumbnails>
)

data class VideosFavouritesFragmentViewState(
        val playlists: ObservableList<VideoPlaylistWithThumbnails> = ObservableSortedList<VideoPlaylistWithThumbnails>(
                VideoPlaylistWithThumbnails::class.java,
                VideoPlaylistWithThumbnails.sortedListCallback
        )
)