package com.example.there.findclips.view.recycler

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView


abstract class EndlessRecyclerOnScrollListener(
        private val visibleThreshold: Int = 5,
        private val returnFromOnScrolledItemCount: Int = 0
) : RecyclerView.OnScrollListener() {
    /**
     * The total number of items in the dataset after the last load
     */
    private var mPreviousTotal = 0
    /**
     * True if we are still waiting for the last set of data to load.
     */
    private var mLoading = true

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        recyclerView?.let {
            val visibleItemCount = recyclerView.childCount
            val totalItemCount = recyclerView.layoutManager.itemCount

            if (totalItemCount == returnFromOnScrolledItemCount)
                return@let

            val firstVisibleItem = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

            if (mLoading) {
                if (totalItemCount > mPreviousTotal) {
                    mLoading = false
                    mPreviousTotal = totalItemCount
                }
            }

            if (!mLoading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
                onLoadMore()
                mLoading = true
            }
        }
    }

    abstract fun onLoadMore()
}