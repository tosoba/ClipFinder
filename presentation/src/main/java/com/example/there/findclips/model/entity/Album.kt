package com.example.there.findclips.model.entity

import android.annotation.SuppressLint
import com.example.there.findclips.util.ObservableSortedList
import io.mironov.smuggler.AutoParcelable

@SuppressLint("ParcelCreator")
data class Album(
        val id: String,
        val name: String,
        val artists: List<SimpleArtist>,
        val albumType: String,
        val iconUrl: String
) : AutoParcelable {
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