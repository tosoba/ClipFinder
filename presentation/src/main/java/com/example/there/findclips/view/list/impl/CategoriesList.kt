package com.example.there.findclips.view.list.impl

import android.databinding.ObservableList
import android.support.v7.widget.RecyclerView
import com.example.there.findclips.databinding.CategoryItemBinding
import com.example.there.findclips.databinding.GridCategoryItemBinding
import com.example.there.findclips.model.entity.Category
import com.example.there.findclips.view.list.base.BaseBindingList
import com.example.there.findclips.view.list.vh.BaseBindingViewHolder

interface CategoriesList {
    class Adapter(categories: ObservableList<Category>, itemLayoutId: Int) :
            BaseBindingList.Adapter<Category, CategoryItemBinding>(categories, itemLayoutId) {

        @Suppress("UNCHECKED_CAST")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            (holder as BaseBindingViewHolder<CategoryItemBinding>).binding.category = items[position]
        }
    }
}

interface GridCategoriesList {
    class Adapter(categories: ObservableList<Category>, itemLayoutId: Int) :
            BaseBindingList.Adapter<Category, GridCategoryItemBinding>(categories, itemLayoutId) {

        @Suppress("UNCHECKED_CAST")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            (holder as BaseBindingViewHolder<GridCategoryItemBinding>).binding.category = items[position]
        }
    }
}
