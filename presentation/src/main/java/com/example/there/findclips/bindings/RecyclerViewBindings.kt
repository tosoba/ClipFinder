package com.example.there.findclips.bindings

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView


@BindingAdapter("itemDecoration")
fun bindItemDecoration(recycler: RecyclerView, decoration: RecyclerView.ItemDecoration) {
    recycler.addItemDecoration(decoration)
}