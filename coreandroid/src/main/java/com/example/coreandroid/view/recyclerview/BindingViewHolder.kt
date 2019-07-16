package com.example.coreandroid.view.recyclerview

import androidx.databinding.ViewDataBinding

open class BindingViewHolder<B>(val binding: B) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) where B : ViewDataBinding