package com.example.there.findclips.model.entity

import android.os.Parcelable
import com.example.there.findclips.R
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.view.imageview.ImageViewSrc
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Artist(
        val id: String,
        val name: String,
        val popularity: Int,
        val iconUrl: String
) : Parcelable {
    val imageViewSrc: ImageViewSrc
        get() = ImageViewSrc(iconUrl, R.drawable.artist_placeholder, R.drawable.error_placeholder)

    companion object {
        val sortedListCallback: ObservableSortedList.Callback<Artist> = object : ObservableSortedList.Callback<Artist> {
            override fun compare(o1: Artist, o2: Artist): Int = o1.name.toLowerCase().compareTo(o2.name.toLowerCase())
            override fun areItemsTheSame(item1: Artist, item2: Artist): Boolean = item1.id == item2.id
            override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean = oldItem.id == newItem.id
        }

        val unsortedListCallback: ObservableSortedList.Callback<Artist> = object : ObservableSortedList.Callback<Artist> {
            override fun compare(o1: Artist, o2: Artist): Int = -1
            override fun areItemsTheSame(item1: Artist, item2: Artist): Boolean = item1.id == item2.id
            override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean = oldItem.id == newItem.id
        }
    }
}

@Parcelize
data class SimpleArtist(
        val id: String,
        val name: String
) : Parcelable