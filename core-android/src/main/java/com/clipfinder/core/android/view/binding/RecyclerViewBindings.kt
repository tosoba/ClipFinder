package com.clipfinder.core.android.view.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("itemDecoration")
fun bindItemDecoration(recycler: RecyclerView, decoration: RecyclerView.ItemDecoration?) {
    decoration?.let(recycler::addItemDecoration)
}

@BindingAdapter("onScrollListener")
fun bindOnScrollListener(recycler: RecyclerView, listener: RecyclerView.OnScrollListener?) {
    listener?.let(recycler::addOnScrollListener)
}

@BindingAdapter("nestedScrollingIsEnabled")
fun bindNestedScrolling(recycler: RecyclerView, nestedScrollingEnabled: Boolean) {
    recycler.isNestedScrollingEnabled = nestedScrollingEnabled
}
