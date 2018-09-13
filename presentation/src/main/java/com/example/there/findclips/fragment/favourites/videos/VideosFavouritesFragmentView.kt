package com.example.there.findclips.fragment.favourites.videos

import android.databinding.ObservableList
import com.example.there.findclips.model.entity.VideoPlaylist
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.view.list.item.RecyclerViewItemView

class VideosFavouritesFragmentView(
        val state: VideosFavouritesFragmentViewState,
        val playlistsRecyclerViewItemView: RecyclerViewItemView<VideoPlaylist>
)

data class VideosFavouritesFragmentViewState(
        val playlists: ObservableList<VideoPlaylist> = ObservableSortedList<VideoPlaylist>(
                VideoPlaylist::class.java,
                VideoPlaylist.sortedListCallback
        )
)