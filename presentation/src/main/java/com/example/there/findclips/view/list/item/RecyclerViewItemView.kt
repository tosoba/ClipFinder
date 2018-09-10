package com.example.there.findclips.view.list.item

import android.databinding.ObservableField
import android.support.v7.widget.RecyclerView

data class RecyclerViewItemView(
        val state: RecyclerViewItemViewState,
        val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
        val itemDecoration: RecyclerView.ItemDecoration?,
        val onScrollListener: RecyclerView.OnScrollListener?
)

data class RecyclerViewItemViewState(
        val initialLoadingInProgress: ObservableField<Boolean>
)