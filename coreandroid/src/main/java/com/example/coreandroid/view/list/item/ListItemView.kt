package com.example.coreandroid.view.list.item

import android.databinding.BaseObservable
import android.databinding.ObservableList
import com.example.coreandroid.view.list.binder.ItemBinder

abstract class ListItemView<T>(val items: ObservableList<T>) : BaseObservable() {
    abstract val itemViewBinder: ItemBinder<T>
}