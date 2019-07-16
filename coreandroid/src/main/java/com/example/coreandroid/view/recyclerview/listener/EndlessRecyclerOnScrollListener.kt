package com.example.coreandroid.view.recyclerview.listener


abstract class EndlessRecyclerOnScrollListener(
        private val visibleThreshold: Int = 5,
        private val minItemsBeforeLoadingMore: Int = 0
) : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
    private var previousItemCount = 0
    private var loading = true

    override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = recyclerView.childCount
        val totalItemCount = recyclerView.layoutManager?.itemCount

        if (totalItemCount == null || totalItemCount <= minItemsBeforeLoadingMore) return

        if (loading) {
            if (totalItemCount > previousItemCount) {
                loading = false
                previousItemCount = totalItemCount
            }
        }

        val firstVisibleItem = (recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findFirstVisibleItemPosition()
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
            onLoadMore()
            loading = true
        }
    }

    abstract fun onLoadMore()
}