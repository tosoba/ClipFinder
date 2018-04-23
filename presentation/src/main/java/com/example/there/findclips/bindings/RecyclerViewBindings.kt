package com.example.there.findclips.bindings

import android.databinding.BindingAdapter
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.there.domain.entities.CategoryEntity
import com.example.there.findclips.dashboard.CategoriesListAdapter

@BindingAdapter("categories")
fun bindCategories(recycler: RecyclerView, categories: ObservableArrayList<CategoryEntity>) {
    recycler.layoutManager = LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)

    categories.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableArrayList<CategoryEntity>>() {
        override fun onChanged(sender: ObservableArrayList<CategoryEntity>?) {
            recycler.adapter.notifyDataSetChanged()
        }

        override fun onItemRangeRemoved(sender: ObservableArrayList<CategoryEntity>?, positionStart: Int, itemCount: Int) {
            recycler.adapter.notifyItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(sender: ObservableArrayList<CategoryEntity>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
            recycler.adapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onItemRangeInserted(sender: ObservableArrayList<CategoryEntity>?, positionStart: Int, itemCount: Int) {
            recycler.adapter.notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeChanged(sender: ObservableArrayList<CategoryEntity>?, positionStart: Int, itemCount: Int) {
            recycler.adapter.notifyItemRangeChanged(positionStart, itemCount)
        }

    })

    recycler.adapter = CategoriesListAdapter(categories)
}