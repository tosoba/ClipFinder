package com.example.coreandroid.view.recyclerview

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

open class BaseBindingViewHolder<B>(val binding: B) : RecyclerView.ViewHolder(binding.root) where B : ViewDataBinding