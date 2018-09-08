package com.example.there.findclips.view.list

import android.databinding.ObservableList
import com.example.there.findclips.databinding.CategoryItemBinding
import com.example.there.findclips.model.entity.Category

interface CategoriesList {
    class Adapter(categories: ObservableList<Category>, itemLayoutId: Int, listener: OnCategoryClickListener) :
            BaseBindingList.Adapter<Category, CategoryItemBinding>(categories, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingList.ViewHolder<CategoryItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.category = items[position]
        }
    }
}

interface OnCategoryClickListener : BaseBindingList.OnItemClickListener<Category>
