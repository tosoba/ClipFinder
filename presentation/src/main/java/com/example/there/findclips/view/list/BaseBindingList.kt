package com.example.there.findclips.view.list

import android.databinding.DataBindingUtil
import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.there.findclips.util.ext.bindToItems

interface BaseBindingList {

    abstract class Adapter<I, B>(
            val items: ObservableList<I>,
            private val itemLayoutId: Int,
            private val onItemClickListener: OnItemClickListener<I>
    ) : RecyclerView.Adapter<BaseBindingViewHolder<B>>() where B : ViewDataBinding {

        init {
            bindToItems(items)
        }

        private fun makeBinding(parent: ViewGroup): B {
            val inflater = LayoutInflater.from(parent.context)
            return DataBindingUtil.inflate(inflater, itemLayoutId, parent, false) as B
        }

        override fun onBindViewHolder(holder: BaseBindingViewHolder<B>, position: Int) {
            holder.binding.root.setOnClickListener { onItemClickListener.onClick(items[position]) }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<B> = BaseBindingViewHolder(makeBinding(parent))

        override fun getItemCount(): Int = items.size
    }
}

