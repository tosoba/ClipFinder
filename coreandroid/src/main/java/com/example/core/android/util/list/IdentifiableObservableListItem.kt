package com.example.core.android.util.list

import java.util.*

interface IdentifiableObservableListItem<Id> {
    val id: Id

    companion object {
        fun <Id, Item : IdentifiableObservableListItem<Id>> unsortedCallback(): ObservableSortedList.Callback<Item> = object : BaseCallback<Id, Item> {
            override fun compare(o1: Item, o2: Item): Int = -1
        }
    }
}

interface IdentifiableNamedObservableListItem<Id> : IdentifiableObservableListItem<Id> {
    val name: String

    companion object {
        fun <Id, Item : IdentifiableNamedObservableListItem<Id>> sortedByNameCallback(): ObservableSortedList.Callback<Item> = object : BaseCallback<Id, Item> {
            override fun compare(o1: Item, o2: Item): Int = o1.name.toLowerCase(Locale.getDefault())
                .compareTo(o2.name.toLowerCase(Locale.getDefault()))
        }
    }
}

interface IdentifiableNumberedObservableListItem<Id> : IdentifiableObservableListItem<Id> {
    val number: Int

    companion object {
        fun <Id, Item : IdentifiableNumberedObservableListItem<Id>> sortedByNumberCallback(): ObservableSortedList.Callback<Item> = object : BaseCallback<Id, Item> {
            override fun compare(o1: Item, o2: Item): Int = o1.number.compareTo(o2.number)
        }
    }
}

private interface BaseCallback<Id, Item : IdentifiableObservableListItem<Id>> : ObservableSortedList.Callback<Item> {
    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem.id == newItem.id
    override fun areItemsTheSame(item1: Item, item2: Item): Boolean = item1.id == item2.id
}
