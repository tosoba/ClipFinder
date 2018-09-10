package com.example.there.findclips.view.list.base

import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.there.findclips.util.ext.bindToItems
import com.example.there.findclips.util.ext.makeItemBinding
import com.example.there.findclips.view.list.vh.BaseBindingViewHolder
import io.reactivex.subjects.PublishSubject

interface BaseBindingList {

    abstract class Adapter<I, B>(
            val items: ObservableList<I>,
            private val itemLayoutId: Int
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() where B : ViewDataBinding {

        init {
            bindToItems(items)
        }

        val itemClicked: PublishSubject<I> = PublishSubject.create()

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as BaseBindingViewHolder<*>).binding.root.setOnClickListener { itemClicked.onNext(items[position]) }
        }

        override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
        ): RecyclerView.ViewHolder = BaseBindingViewHolder<B>(parent.makeItemBinding(itemLayoutId))

        override fun getItemCount(): Int = items.size
    }
}

