package com.clipfinder.core.android.view.recyclerview.item

import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.RecyclerView
import com.clipfinder.core.android.view.recyclerview.listener.ClickHandler

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
    val items: ObservableList<T>,
    val loadingErrorOccurred: ObservableField<Boolean>
)
