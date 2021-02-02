package com.clipfinder.core.android.view.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.clipfinder.core.android.view.recyclerview.BindingRecyclerViewAdapter
import com.clipfinder.core.android.view.recyclerview.binder.ItemBinder
import com.clipfinder.core.android.view.recyclerview.listener.ClickHandler

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

private const val KEY_ITEMS = -123
private const val KEY_CLICK_HANDLER = -124

@Suppress("UNCHECKED_CAST")
@BindingAdapter("items")
fun <T> setItems(recyclerView: RecyclerView, items: Collection<T>) {
    val adapter = recyclerView.adapter as? _root_ide_package_.com.clipfinder.core.android.view.recyclerview.BindingRecyclerViewAdapter<T>?
    if (adapter != null) {
        adapter.setItems(items)
    } else {
        recyclerView.setTag(KEY_ITEMS, items)
    }
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("clickHandler")
fun <T> setHandler(recyclerView: RecyclerView, handler: _root_ide_package_.com.clipfinder.core.android.view.recyclerview.listener.ClickHandler<T>) {
    val adapter = recyclerView.adapter as? _root_ide_package_.com.clipfinder.core.android.view.recyclerview.BindingRecyclerViewAdapter<T>?
    if (adapter != null) {
        adapter.setClickHandler(handler)
    } else {
        recyclerView.setTag(KEY_CLICK_HANDLER, handler)
    }
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("itemViewBinder")
fun <T> setItemViewBinder(recyclerView: RecyclerView, itemViewMapper: _root_ide_package_.com.clipfinder.core.android.view.recyclerview.binder.ItemBinder<T>) {
    val items = recyclerView.getTag(KEY_ITEMS) as? Collection<T>
    val clickHandler = recyclerView.getTag(KEY_CLICK_HANDLER) as? _root_ide_package_.com.clipfinder.core.android.view.recyclerview.listener.ClickHandler<T>
    val adapter = _root_ide_package_.com.clipfinder.core.android.view.recyclerview.BindingRecyclerViewAdapter(itemViewMapper, items)
    if (clickHandler != null) {
        adapter.setClickHandler(clickHandler)
    }
    recyclerView.adapter = adapter
}
