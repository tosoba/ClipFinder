package com.example.there.findclips.bindings

import android.databinding.BindingAdapter
import android.widget.SearchView

@BindingAdapter("onQueryTextListener")
fun bindOnQueryTextListener(searchView: SearchView, listener: SearchView.OnQueryTextListener) {
    searchView.setOnQueryTextListener(listener)
}