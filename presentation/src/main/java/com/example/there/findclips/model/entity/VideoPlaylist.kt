package com.example.there.findclips.model.entity

import android.annotation.SuppressLint
import com.example.there.findclips.util.ObservableSortedList
import io.mironov.smuggler.AutoParcelable

@SuppressLint("ParcelCreator")
data class VideoPlaylist(val id: Long = 0, val name: String): AutoParcelable {
    companion object {
        val sortedListCallback: ObservableSortedList.Callback<VideoPlaylist> = object : ObservableSortedList.Callback<VideoPlaylist> {
            override fun compare(o1: VideoPlaylist, o2: VideoPlaylist): Int = o1.name.toLowerCase().compareTo(o2.name.toLowerCase())
            override fun areItemsTheSame(item1: VideoPlaylist, item2: VideoPlaylist): Boolean = item1 == item2
            override fun areContentsTheSame(oldItem: VideoPlaylist, newItem: VideoPlaylist): Boolean = oldItem == newItem
        }
    }
}