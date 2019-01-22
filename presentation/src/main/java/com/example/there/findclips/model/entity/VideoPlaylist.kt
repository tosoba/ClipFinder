package com.example.there.findclips.model.entity

import android.os.Parcelable
import com.example.there.findclips.util.ObservableSortedList
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VideoPlaylist(val id: Long? = null, val name: String): Parcelable {
    companion object {
        val sortedListCallback: ObservableSortedList.Callback<VideoPlaylist> = object : ObservableSortedList.Callback<VideoPlaylist> {
            override fun compare(o1: VideoPlaylist, o2: VideoPlaylist): Int = o1.name.toLowerCase().compareTo(o2.name.toLowerCase())
            override fun areItemsTheSame(item1: VideoPlaylist, item2: VideoPlaylist): Boolean = item1 == item2
            override fun areContentsTheSame(oldItem: VideoPlaylist, newItem: VideoPlaylist): Boolean = oldItem == newItem
        }
    }
}