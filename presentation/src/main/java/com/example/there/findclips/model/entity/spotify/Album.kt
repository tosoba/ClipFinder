package com.example.there.findclips.model.entity.spotify

import android.os.Parcelable
import com.example.there.findclips.R
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.view.imageview.ImageViewSrc
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Album(
        val id: String,
        val name: String,
        val artists: List<SimpleArtist>,
        val albumType: String,
        val iconUrl: String,
        val uri: String
) : Parcelable {
    val imageViewSrc: ImageViewSrc
        get() = ImageViewSrc(iconUrl, R.drawable.album_placeholder, R.drawable.error_placeholder)

    companion object {
        val sortedListCallback: ObservableSortedList.Callback<Album> = object : ObservableSortedList.Callback<Album> {
            override fun compare(o1: Album, o2: Album): Int = o1.name.toLowerCase().compareTo(o2.name.toLowerCase())
            override fun areItemsTheSame(item1: Album, item2: Album): Boolean = item1.id == item2.id
            override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean = oldItem.id == newItem.id
        }

        val unsortedListCallback: ObservableSortedList.Callback<Album> = object : ObservableSortedList.Callback<Album> {
            override fun compare(o1: Album, o2: Album): Int = -1
            override fun areItemsTheSame(item1: Album, item2: Album): Boolean = item1.id == item2.id
            override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean = oldItem.id == newItem.id
        }
    }
}