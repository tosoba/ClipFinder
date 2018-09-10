package com.example.there.findclips.view.list.impl

import android.databinding.ObservableList
import com.example.there.findclips.databinding.CategoryItemBinding
import com.example.there.findclips.model.entity.Category
import com.example.there.findclips.view.list.base.BaseBindingList
import com.example.there.findclips.view.list.vh.BaseBindingViewHolder

interface CategoriesList {
    class Adapter(categories: ObservableList<Category>, itemLayoutId: Int) :
            BaseBindingList.Adapter<Category, CategoryItemBinding>(categories, itemLayoutId) {

        override fun onBindViewHolder(holder: BaseBindingViewHolder<CategoryItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.category = items[position]
        }
    }
}

