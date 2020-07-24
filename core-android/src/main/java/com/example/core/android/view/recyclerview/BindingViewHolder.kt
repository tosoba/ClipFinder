package com.example.core.android.view.recyclerview

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BindingViewHolder<B>(val binding: B) : RecyclerView.ViewHolder(binding.root) where B : ViewDataBinding
