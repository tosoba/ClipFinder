package com.example.there.findclips.lists

import android.databinding.ObservableArrayList
import com.example.there.domain.entities.spotify.CategoryEntity
import com.example.there.findclips.databinding.CategoryItemBinding

interface CategoriesList {
    class Adapter(categories: ObservableArrayList<CategoryEntity>, itemLayoutId: Int, listener: OnItemClickListener) :
            BaseBindingList.Adapter<CategoryEntity, CategoryItemBinding>(categories, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingList.ViewHolder<CategoryItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.category = items[position]
        }
    }

    interface OnItemClickListener : BaseBindingList.OnItemClickListener<CategoryEntity>
}
