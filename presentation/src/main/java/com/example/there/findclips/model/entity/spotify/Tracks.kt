package com.example.there.findclips.model.entity.spotify

import android.os.Parcelable
import com.example.there.findclips.R
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.view.imageview.ImageViewSrc
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Track(
        val id: String,
        val name: String,
        val iconUrl: String,
        val albumId: String,
        val albumName: String,
        val artists: List<SimpleArtist>,
        val popularity: Int,
        val trackNumber: Int,
        val uri: String,
        val durationMs: Int
) : Parcelable {

    val imageViewSrc: ImageViewSrc
        get() = ImageViewSrc(iconUrl, R.drawable.track_placeholder, R.drawable.error_placeholder)

    val query: String
        get() = "$name $albumName"

    companion object {
        val sortedListCallbackName: ObservableSortedList.Callback<Track> = object : ObservableSortedList.Callback<Track> {
            override fun compare(o1: Track, o2: Track): Int = o1.name.toLowerCase().compareTo(o2.name.toLowerCase())
            override fun areItemsTheSame(item1: Track, item2: Track): Boolean = item1.id == item2.id
            override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean = oldItem.id == newItem.id
        }

        val sortedListCallbackTrackNumber: ObservableSortedList.Callback<Track> = object : ObservableSortedList.Callback<Track> {
            override fun compare(o1: Track, o2: Track): Int = o1.trackNumber.compareTo(o2.trackNumber)
            override fun areItemsTheSame(item1: Track, item2: Track): Boolean = item1.id == item2.id
            override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean = oldItem.id == newItem.id
        }

        val unsortedListCallback: ObservableSortedList.Callback<Track> = object : ObservableSortedList.Callback<Track> {
            override fun compare(o1: Track, o2: Track): Int = -1
            override fun areItemsTheSame(item1: Track, item2: Track): Boolean = item1.id == item2.id
            override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean = oldItem.id == newItem.id
        }
    }
}

@Parcelize
data class TopTrack(val position: Int, val track: Track) : Parcelable {
    companion object {
        val sortedListCallback = object : ObservableSortedList.Callback<TopTrack> {
            override fun compare(o1: TopTrack, o2: TopTrack): Int = o1.position.compareTo(o2.position)
            override fun areItemsTheSame(item1: TopTrack, item2: TopTrack): Boolean = item1.track.id == item2.track.id
            override fun areContentsTheSame(oldItem: TopTrack, newItem: TopTrack): Boolean = oldItem.track.id == newItem.track.id
        }
    }
}