package com.example.there.findclips.model.entity

import android.annotation.SuppressLint
import com.example.there.findclips.R
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.view.imageview.ImageViewSrc
import io.mironov.smuggler.AutoParcelable

@SuppressLint("ParcelCreator")
data class Playlist(
        val id: String,
        val name: String,
        val iconUrl: String,
        val userId: String,
        val uri: String
) : AutoParcelable {
    val imageViewSrc: ImageViewSrc
        get() = ImageViewSrc(iconUrl, R.drawable.playlist_placeholder, R.drawable.error_placeholder)

    companion object {
        val sortedListCallback: ObservableSortedList.Callback<Playlist> = object : ObservableSortedList.Callback<Playlist> {
            override fun compare(o1: Playlist, o2: Playlist): Int = o1.name.toLowerCase().compareTo(o2.name.toLowerCase())
            override fun areItemsTheSame(item1: Playlist, item2: Playlist): Boolean = item1.id == item2.id
            override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean = oldItem.id == newItem.id
        }

        val unsortedListCallback: ObservableSortedList.Callback<Playlist> = object : ObservableSortedList.Callback<Playlist> {
            override fun compare(o1: Playlist, o2: Playlist): Int = -1
            override fun areItemsTheSame(item1: Playlist, item2: Playlist): Boolean = item1.id == item2.id
            override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean = oldItem.id == newItem.id
        }
    }
}