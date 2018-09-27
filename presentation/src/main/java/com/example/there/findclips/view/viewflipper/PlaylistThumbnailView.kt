package com.example.there.findclips.view.viewflipper

import com.example.there.findclips.model.entity.VideoPlaylist
import com.example.there.findclips.util.ObservableSortedList

class PlaylistThumbnailView(
        val playlist: VideoPlaylist,
        val adapter: PlaylistThumbnailFlipperAdapter
) {
    companion object {
        val sortedListCallback: ObservableSortedList.Callback<PlaylistThumbnailView> = object : ObservableSortedList.Callback<PlaylistThumbnailView> {
            override fun compare(o1: PlaylistThumbnailView, o2: PlaylistThumbnailView): Int = o1.playlist.name.toLowerCase().compareTo(o2.playlist.name.toLowerCase())
            override fun areItemsTheSame(item1: PlaylistThumbnailView, item2: PlaylistThumbnailView): Boolean = item1.playlist.id == item2.playlist.id
            override fun areContentsTheSame(oldItem: PlaylistThumbnailView, newItem: PlaylistThumbnailView): Boolean = oldItem.playlist.id == newItem.playlist.id
        }
    }
}