package com.example.there.findclips.view.list.item

import android.databinding.ObservableField
import android.databinding.ObservableList
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.there.findclips.view.list.ClickHandler

class RecyclerViewItemView<T>(
        val state: RecyclerViewItemViewState<T>,
        val listItemView: ListItemView<T>,
        val onItemClickListener: ClickHandler<T>,
        val itemDecoration: RecyclerView.ItemDecoration? = null,
        val onScrollListener: RecyclerView.OnScrollListener? = null,
        val onReloadBtnClickListener: View.OnClickListener? = null,
        val nestedScrollingEnabled: Boolean = false
)

class RecyclerViewItemViewState<T>(
        val initialLoadingInProgress: ObservableField<Boolean>,
        val items: ObservableList<T>
)