package com.example.there.findclips.model.entity

import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.view.binding.CollageViewState

data class VideoPlaylistWithThumbnails(
        val playlist: VideoPlaylist,
        val collageViewState: CollageViewState
) {
    companion object {
        val sortedListCallback = object : ObservableSortedList.Callback<VideoPlaylistWithThumbnails> {
            override fun compare(
                    o1: VideoPlaylistWithThumbnails,
                    o2: VideoPlaylistWithThumbnails
            ): Int = o1.playlist.name.toLowerCase().compareTo(o2.playlist.name.toLowerCase())

            override fun areItemsTheSame(
                    item1: VideoPlaylistWithThumbnails,
                    item2: VideoPlaylistWithThumbnails
            ): Boolean = item1.playlist == item2.playlist

            override fun areContentsTheSame(
                    oldItem: VideoPlaylistWithThumbnails,
                    newItem: VideoPlaylistWithThumbnails
            ): Boolean = oldItem.playlist == newItem.playlist
        }
    }
}