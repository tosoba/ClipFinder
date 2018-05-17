package com.example.there.findclips.view.lists

import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

interface BaseBindingList {

    abstract class Adapter<I, B>(val items: ObservableArrayList<I>,
                                 private val itemLayoutId: Int,
                                 private val onItemClickListener: OnItemClickListener<I>) : RecyclerView.Adapter<ViewHolder<B>>() where B : ViewDataBinding {
        init {
            items.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableArrayList<I>>() {
                override fun onChanged(sender: ObservableArrayList<I>?) = notifyDataSetChanged()

                override fun onItemRangeRemoved(sender: ObservableArrayList<I>?, positionStart: Int, itemCount: Int) =
                        notifyItemRangeRemoved(positionStart, itemCount)

                override fun onItemRangeMoved(sender: ObservableArrayList<I>?, fromPosition: Int, toPosition: Int, itemCount: Int) =
                        notifyItemMoved(fromPosition, toPosition)

                override fun onItemRangeInserted(sender: ObservableArrayList<I>?, positionStart: Int, itemCount: Int) =
                        notifyItemRangeInserted(positionStart, itemCount)

                override fun onItemRangeChanged(sender: ObservableArrayList<I>?, positionStart: Int, itemCount: Int) =
                        notifyItemRangeChanged(positionStart, itemCount)
            })
        }

        private fun makeBinding(parent: ViewGroup): B {
            val inflater = LayoutInflater.from(parent.context)
            return DataBindingUtil.inflate(inflater, itemLayoutId, parent, false) as B
        }

        override fun onBindViewHolder(holder: ViewHolder<B>, position: Int) {
            holder.binding.root.setOnClickListener { onItemClickListener.onClick(items[position]) }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<B> = ViewHolder(makeBinding(parent))

        override fun getItemCount(): Int = items.size
    }

    open class ViewHolder<B>(val binding: B) : RecyclerView.ViewHolder(binding.root) where B : ViewDataBinding

    interface OnItemClickListener<I> {
        fun onClick(item: I)
    }
}

