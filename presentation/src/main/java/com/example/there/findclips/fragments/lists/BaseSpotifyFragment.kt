package com.example.there.findclips.fragments.lists

import android.databinding.ObservableArrayList
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment

abstract class BaseSpotifyFragment<T : Parcelable>: Fragment() {

    fun addItems(items: List<T>) {
        if (viewState.items.isNotEmpty()) viewState.items.clear()
        viewState.items.addAll(items)
    }

    abstract val viewState: ViewState<T>

    data class ViewState<T : Parcelable>(val items: ObservableArrayList<T> = ObservableArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_SAVED_ITEMS)) {
            viewState.items.addAll(savedInstanceState.getParcelableArrayList(KEY_SAVED_ITEMS))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (viewState.items.isNotEmpty()) {
            outState.putParcelableArrayList(KEY_SAVED_ITEMS, viewState.items)
        }
    }

    companion object {
        private const val KEY_SAVED_ITEMS = "KEY_SAVED_ITEMS"
    }
}