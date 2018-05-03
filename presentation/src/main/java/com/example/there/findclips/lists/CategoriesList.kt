package com.example.there.findclips.lists

import android.databinding.ObservableArrayList
import com.example.there.findclips.databinding.CategoryItemBinding
import com.example.there.findclips.entities.Category

interface CategoriesList {
    class Adapter(categories: ObservableArrayList<Category>, itemLayoutId: Int, listener: OnItemClickListener) :
            BaseBindingList.Adapter<Category, CategoryItemBinding>(categories, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingList.ViewHolder<CategoryItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.category = items[position]
        }
    }

    interface OnItemClickListener : BaseBindingList.OnItemClickListener<Category>
}
