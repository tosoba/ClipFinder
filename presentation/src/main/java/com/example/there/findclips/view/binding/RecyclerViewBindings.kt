package com.example.there.findclips.view.binding

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView

@BindingAdapter("itemDecoration")
fun bindItemDecoration(recycler: RecyclerView, decoration: RecyclerView.ItemDecoration?) {
    decoration?.let { recycler.addItemDecoration(it) }
}

@BindingAdapter("onScrollListener")
fun bindOnScrollListener(recycler: RecyclerView, listener: RecyclerView.OnScrollListener?) {
    listener?.let { recycler.addOnScrollListener(it) }
}

@BindingAdapter("hasFixedSize")
fun bindHasFixedSize(recycler: RecyclerView, hasFixedSize: Boolean) = recycler.setHasFixedSize(hasFixedSize)

@BindingAdapter("nestedScrollingIsEnabled")
fun bindNestedScrolling(recycler: RecyclerView, nestedScrollingEnabled: Boolean) {
    recycler.isNestedScrollingEnabled = nestedScrollingEnabled
}