package com.example.there.findclips.view.list.item

import android.databinding.ObservableField
import android.support.v7.widget.RecyclerView
import android.view.View

data class RecyclerViewItemView(
        val state: RecyclerViewItemViewState,
        val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
        val itemDecoration: RecyclerView.ItemDecoration?,
        val onScrollListener: RecyclerView.OnScrollListener?,
        val onReloadBtnClickListener: View.OnClickListener? = null,
        val nestedScrollingEnabled: Boolean = false
)

data class RecyclerViewItemViewState(
        val initialLoadingInProgress: ObservableField<Boolean>,
        val errorOccurred: ObservableField<Boolean> = ObservableField(false)
)