package com.example.there.findclips.model.entity

import android.annotation.SuppressLint
import com.example.there.findclips.R
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.view.imageview.ImageViewSrc
import io.mironov.smuggler.AutoParcelable

@SuppressLint("ParcelCreator")
data class Category(
        val id: String,
        val name: String,
        val iconUrl: String
) : AutoParcelable {
    val imageViewSrc: ImageViewSrc
        get() = ImageViewSrc(iconUrl, R.drawable.category_placeholder, R.drawable.error_placeholder)

    companion object {
        val sortedListCallback: ObservableSortedList.Callback<Category> = object : ObservableSortedList.Callback<Category> {
            override fun compare(o1: Category, o2: Category): Int = o1.name.toLowerCase().compareTo(o2.name.toLowerCase())
            override fun areItemsTheSame(item1: Category, item2: Category): Boolean = item1.id == item2.id
            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean = oldItem.id == newItem.id
        }

        val unsortedListCallback: ObservableSortedList.Callback<Category> = object : ObservableSortedList.Callback<Category> {
            override fun compare(o1: Category, o2: Category): Int = -1
            override fun areItemsTheSame(item1: Category, item2: Category): Boolean = item1.id == item2.id
            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean = oldItem.id == newItem.id
        }
    }
}