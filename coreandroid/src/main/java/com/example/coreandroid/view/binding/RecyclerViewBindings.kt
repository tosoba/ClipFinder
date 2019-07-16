package com.example.coreandroid.view.binding

import androidx.databinding.BindingAdapter
import com.example.coreandroid.view.recyclerview.BindingRecyclerViewAdapter
import com.example.coreandroid.view.recyclerview.binder.ItemBinder
import com.example.coreandroid.view.recyclerview.listener.ClickHandler
import com.example.coreandroid.view.recyclerview.listener.LongClickHandler


@BindingAdapter("itemDecoration")
fun bindItemDecoration(recycler: androidx.recyclerview.widget.RecyclerView, decoration: androidx.recyclerview.widget.RecyclerView.ItemDecoration?) {
    decoration?.let { recycler.addItemDecoration(it) }
}

@BindingAdapter("onScrollListener")
fun bindOnScrollListener(recycler: androidx.recyclerview.widget.RecyclerView, listener: androidx.recyclerview.widget.RecyclerView.OnScrollListener?) {
    listener?.let { recycler.addOnScrollListener(it) }
}

@BindingAdapter("hasFixedSize")
fun bindHasFixedSize(recycler: androidx.recyclerview.widget.RecyclerView, hasFixedSize: Boolean) = recycler.setHasFixedSize(hasFixedSize)

@BindingAdapter("nestedScrollingIsEnabled")
fun bindNestedScrolling(recycler: androidx.recyclerview.widget.RecyclerView, nestedScrollingEnabled: Boolean) {
    recycler.isNestedScrollingEnabled = nestedScrollingEnabled
}


private const val KEY_ITEMS = -123
private const val KEY_CLICK_HANDLER = -124
private const val KEY_LONG_CLICK_HANDLER = -125

@Suppress("UNCHECKED_CAST")
@BindingAdapter("items")
fun <T> setItems(recyclerView: androidx.recyclerview.widget.RecyclerView, items: Collection<T>) {
    val adapter = recyclerView.adapter as? BindingRecyclerViewAdapter<T>?
    if (adapter != null) {
        adapter.setItems(items)
    } else {
        recyclerView.setTag(KEY_ITEMS, items)
    }
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("clickHandler")
fun <T> setHandler(recyclerView: androidx.recyclerview.widget.RecyclerView, handler: ClickHandler<T>) {
    val adapter = recyclerView.adapter as? BindingRecyclerViewAdapter<T>?
    if (adapter != null) {
        adapter.setClickHandler(handler)
    } else {
        recyclerView.setTag(KEY_CLICK_HANDLER, handler)
    }
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("longClickHandler")
fun <T> setHandler(recyclerView: androidx.recyclerview.widget.RecyclerView, handler: LongClickHandler<T>) {
    val adapter = recyclerView.adapter as? BindingRecyclerViewAdapter<T>?
    if (adapter != null) {
        adapter.setLongClickHandler(handler)
    } else {
        recyclerView.setTag(KEY_LONG_CLICK_HANDLER, handler)
    }
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("itemViewBinder")
fun <T> setItemViewBinder(recyclerView: androidx.recyclerview.widget.RecyclerView, itemViewMapper: ItemBinder<T>) {
    val items = recyclerView.getTag(KEY_ITEMS) as? Collection<T>
    val clickHandler = recyclerView.getTag(KEY_CLICK_HANDLER) as? ClickHandler<T>
    val adapter = BindingRecyclerViewAdapter(itemViewMapper, items)
    if (clickHandler != null) {
        adapter.setClickHandler(clickHandler)
    }
    recyclerView.adapter = adapter
}