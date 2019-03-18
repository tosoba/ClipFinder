package com.example.coreandroid.view.viewflipper

import android.view.View
import com.example.coreandroid.model.videos.VideoPlaylist
import com.example.coreandroid.util.list.ObservableSortedList

class PlaylistThumbnailView(
        val playlist: VideoPlaylist,
        val adapter: PlaylistThumbnailFlipperAdapter,
        val onRemoveBtnClickListener: View.OnClickListener
) {
    //TODO: use common callback
    companion object {
        val sortedListCallback: ObservableSortedList.Callback<PlaylistThumbnailView> = object : ObservableSortedList.Callback<PlaylistThumbnailView> {
            override fun compare(o1: PlaylistThumbnailView, o2: PlaylistThumbnailView): Int = o1.playlist.name.toLowerCase().compareTo(o2.playlist.name.toLowerCase())
            override fun areItemsTheSame(item1: PlaylistThumbnailView, item2: PlaylistThumbnailView): Boolean = item1.playlist.id == item2.playlist.id
            override fun areContentsTheSame(oldItem: PlaylistThumbnailView, newItem: PlaylistThumbnailView): Boolean = oldItem.playlist.id == newItem.playlist.id
        }
    }
}