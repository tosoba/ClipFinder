package com.example.core.android.view.recyclerview.item

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableList
import com.example.core.android.view.recyclerview.binder.ItemBinder

abstract class ListItemView<T>(val items: ObservableList<T>) : BaseObservable() {
    abstract val itemViewBinder: ItemBinder<T>
}