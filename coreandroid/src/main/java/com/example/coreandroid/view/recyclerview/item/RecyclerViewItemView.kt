package com.example.coreandroid.view.recyclerview.item

import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import com.example.coreandroid.view.recyclerview.listener.ClickHandler

class RecyclerViewItemView<T>(
        val state: RecyclerViewItemViewState<T>,
        val listItemView: ListItemView<T>,
        val onItemClickListener: ClickHandler<T>,
        val itemDecoration: androidx.recyclerview.widget.RecyclerView.ItemDecoration? = null,
        val onScrollListener: androidx.recyclerview.widget.RecyclerView.OnScrollListener? = null,
        val onReloadBtnClickListener: View.OnClickListener? = null,
        val nestedScrollingEnabled: Boolean = false
)

class RecyclerViewItemViewState<T>(
        val initialLoadingInProgress: ObservableField<Boolean>,
        val items: ObservableList<T>,
        val loadingErrorOccurred: ObservableField<Boolean>
)