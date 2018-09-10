package com.example.there.findclips.view.list.vh

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

open class BaseBindingViewHolder<B>(val binding: B) : RecyclerView.ViewHolder(binding.root) where B : ViewDataBinding