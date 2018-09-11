package com.example.there.findclips.view.list.base

import android.databinding.ObservableField
import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.databinding.LoadingItemBinding
import com.example.there.findclips.util.ext.bindToItems
import com.example.there.findclips.util.ext.makeItemBinding
import com.example.there.findclips.view.list.item.LoadingItemViewState
import com.example.there.findclips.view.list.vh.BaseBindingViewHolder
import io.reactivex.subjects.PublishSubject

interface BaseBindingLoadingList {

    abstract class Adapter<I, B>(
            val items: ObservableList<I>,
            @LayoutRes private val itemLayoutId: Int,
            private val loadingMoreItemsInProgress: ObservableField<Boolean> = ObservableField(false)
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() where B : ViewDataBinding {

        init {
            bindToItems(items)
        }

        val itemClicked: PublishSubject<I> = PublishSubject.create()

        var recyclerView: RecyclerView? = null
            private set

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            super.onAttachedToRecyclerView(recyclerView)
            this.recyclerView = recyclerView
        }

        fun scrollToTop() = recyclerView?.scrollToPosition(0)

        override fun getItemViewType(position: Int): Int = if (position == items.size) VIEW_TYPE_LOADING
        else VIEW_TYPE_ITEM

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (position < items.size) {
                val itemViewHolder = holder as? BaseBindingViewHolder<*>
                itemViewHolder?.binding?.root?.setOnClickListener {
                    itemClicked.onNext(items[position])
                }
            }
        }

        override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
        ): RecyclerView.ViewHolder = when (viewType) {
            VIEW_TYPE_ITEM -> BaseBindingViewHolder(parent.makeItemBinding<B>(itemLayoutId))
            VIEW_TYPE_LOADING -> BaseBindingViewHolder(parent.makeItemBinding<LoadingItemBinding>(R.layout.loading_item).apply {
                viewState = LoadingItemViewState(loadingMoreItemsInProgress)
            })
            else -> throw IllegalStateException("${javaClass.name}: Unknown viewType: $viewType")
        }

        override fun getItemCount(): Int = items.size + 1
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }
}