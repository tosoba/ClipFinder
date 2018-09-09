package com.example.there.findclips.view.list

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.databinding.LoadingItemBinding
import com.example.there.findclips.util.ext.bindToItems

interface BaseBindingLoadingList {

    abstract class Adapter<I, B>(
            val items: ObservableList<I>,
            @LayoutRes private val itemLayoutId: Int,
            private val onItemClickListener: OnItemClickListener<I>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() where B : ViewDataBinding {

        init {
            bindToItems(items)
        }

        val loadingInProgress: ObservableField<Boolean> = ObservableField(false)

        private fun <T : ViewDataBinding> makeItemBinding(
                parent: ViewGroup,
                @LayoutRes layoutId: Int
        ): T = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                layoutId,
                parent,
                false
        ) as T

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (position < items.size) {
                val itemViewHolder = holder as? BaseBindingViewHolder<*>
                itemViewHolder?.binding?.root?.setOnClickListener {
                    onItemClickListener.onClick(items[position])
                }
            }
        }

        override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
        ): RecyclerView.ViewHolder = when (viewType) {
            VIEW_TYPE_ITEM -> BaseBindingViewHolder(makeItemBinding<B>(parent, itemLayoutId))
            VIEW_TYPE_LOADING -> BaseBindingViewHolder(makeItemBinding<LoadingItemBinding>(
                    parent,
                    R.layout.loading_item
            ).apply { viewState = LoadingItemViewState(loadingInProgress) })
            else -> throw IllegalStateException("${javaClass.name}: Unknown viewType: $viewType")
        }

        override fun getItemCount(): Int = items.size + 1
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }
}