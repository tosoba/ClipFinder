package com.example.coreandroid.view.recyclerview.listener

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class EndlessRecyclerOnScrollListener(
        private val visibleThreshold: Int = 5,
        private val minItemsBeforeLoadingMore: Int = 0,
        private val onLoadMore: () -> Unit
) : RecyclerView.OnScrollListener() {
    private var previousItemCount = 0
    private var loading = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = recyclerView.childCount
        val totalItemCount = recyclerView.layoutManager?.itemCount

        if (totalItemCount == null || totalItemCount <= minItemsBeforeLoadingMore) return

        if (loading && totalItemCount > previousItemCount) {
            loading = false
            previousItemCount = totalItemCount
        }

        val firstVisibleItem = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
            onLoadMore()
            loading = true
        }
    }
}