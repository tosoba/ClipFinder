package com.example.there.findclips.util.ext

import android.databinding.ObservableList
import android.support.v7.widget.RecyclerView

fun <I, VH> RecyclerView.Adapter<VH>.bindToItems(
        items: ObservableList<I>,
        offset: Int = 0
) where VH : RecyclerView.ViewHolder {
    items.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableList<I>>() {

        override fun onChanged(sender: ObservableList<I>?) = notifyDataSetChanged()

        override fun onItemRangeRemoved(
                sender: ObservableList<I>?,
                positionStart: Int,
                itemCount: Int
        ) = notifyItemRangeRemoved(positionStart + offset, itemCount)

        override fun onItemRangeMoved(
                sender: ObservableList<I>?,
                fromPosition: Int,
                toPosition: Int,
                itemCount: Int
        ) = notifyItemMoved(fromPosition + offset, toPosition + offset)

        override fun onItemRangeInserted(
                sender: ObservableList<I>?,
                positionStart: Int,
                itemCount: Int
        ) = notifyItemRangeInserted(positionStart + offset, itemCount)

        override fun onItemRangeChanged(
                sender: ObservableList<I>?,
                positionStart: Int,
                itemCount: Int
        ) = notifyItemRangeChanged(positionStart + offset, itemCount)
    })
}