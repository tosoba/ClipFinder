package com.example.there.findclips.model.entities

import android.annotation.SuppressLint
import com.example.there.findclips.util.ObservableSortedList
import io.mironov.smuggler.AutoParcelable

@SuppressLint("ParcelCreator")
data class Category(
        val id: String,
        val name: String,
        val iconUrl: String
) : AutoParcelable {
    companion object {
        val sortedListCallback: ObservableSortedList.Callback<Category> = object : ObservableSortedList.Callback<Category> {
            override fun compare(o1: Category, o2: Category): Int = o1.name.toLowerCase().compareTo(o2.name.toLowerCase())
            override fun areItemsTheSame(item1: Category, item2: Category): Boolean = item1.id == item2.id
            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean = oldItem.id == newItem.id
        }
    }
}