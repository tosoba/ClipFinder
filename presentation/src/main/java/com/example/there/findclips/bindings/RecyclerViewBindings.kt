package com.example.there.findclips.bindings

import android.databinding.BindingAdapter
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.there.domain.entities.CategoryEntity
import com.example.there.domain.entities.PlaylistEntity
import com.example.there.findclips.dashboard.adapter.CategoriesListAdapter
import com.example.there.findclips.dashboard.adapter.PlaylistsListAdapter

fun <T> makeOnListChangedCallback(recycler: RecyclerView): ObservableList.OnListChangedCallback<ObservableArrayList<T>> =
        object : ObservableList.OnListChangedCallback<ObservableArrayList<T>>() {
            override fun onChanged(sender: ObservableArrayList<T>?) {
                recycler.adapter.notifyDataSetChanged()
            }

            override fun onItemRangeRemoved(sender: ObservableArrayList<T>?, positionStart: Int, itemCount: Int) {
                recycler.adapter.notifyItemRangeRemoved(positionStart, itemCount)
            }

            override fun onItemRangeMoved(sender: ObservableArrayList<T>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
                recycler.adapter.notifyItemMoved(fromPosition, toPosition)
            }

            override fun onItemRangeInserted(sender: ObservableArrayList<T>?, positionStart: Int, itemCount: Int) {
                recycler.adapter.notifyItemRangeInserted(positionStart, itemCount)
            }

            override fun onItemRangeChanged(sender: ObservableArrayList<T>?, positionStart: Int, itemCount: Int) {
                recycler.adapter.notifyItemRangeChanged(positionStart, itemCount)
            }
        }

@BindingAdapter("featuredPlaylists")
fun bindFeaturedPlaylists(recycler: RecyclerView, playlists: ObservableArrayList<PlaylistEntity>) {
    recycler.layoutManager = GridLayoutManager(recycler.context, 2, GridLayoutManager.HORIZONTAL, false)
    playlists.addOnListChangedCallback(makeOnListChangedCallback<PlaylistEntity>(recycler))
    recycler.adapter = PlaylistsListAdapter(playlists)
}

@BindingAdapter("categories")
fun bindCategories(recycler: RecyclerView, categories: ObservableArrayList<CategoryEntity>) {
    recycler.layoutManager = GridLayoutManager(recycler.context, 2, GridLayoutManager.HORIZONTAL, false)
    categories.addOnListChangedCallback(makeOnListChangedCallback<CategoryEntity>(recycler))
    recycler.adapter = CategoriesListAdapter(categories)
}

