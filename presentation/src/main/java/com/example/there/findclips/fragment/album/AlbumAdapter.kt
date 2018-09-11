package com.example.there.findclips.fragment.album

import android.databinding.ObservableField
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.databinding.HeaderItemBinding
import com.example.there.findclips.databinding.RecyclerViewListItemBinding
import com.example.there.findclips.util.ext.makeItemBinding
import com.example.there.findclips.view.list.item.HeaderItemViewState
import com.example.there.findclips.view.list.item.RecyclerViewItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemViewState
import com.example.there.findclips.view.list.vh.BaseBindingViewHolder

class AlbumAdapter(
        private val artistsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
        private val tracksAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
        private val artistsLoadingInProgress: ObservableField<Boolean>,
        private val tracksLoadingInProgress: ObservableField<Boolean>,
        private val onTracksScrollListener: RecyclerView.OnScrollListener,
        private val tracksItemDecoration: RecyclerView.ItemDecoration
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int = viewTypes[position]

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): RecyclerView.ViewHolder = when (viewType) {
        ARTISTS_HEADER_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
            viewState = HeaderItemViewState("Artists")
        })
        ARTISTS_LIST_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
            view = RecyclerViewItemView(RecyclerViewItemViewState(artistsLoadingInProgress), artistsAdapter, null, null)
            itemRecyclerView.layoutManager = LinearLayoutManager(parent.context, LinearLayoutManager.HORIZONTAL, false)
            itemRecyclerView.isNestedScrollingEnabled = false
        })
        TRACKS_HEADER_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
            viewState = HeaderItemViewState("Tracks")
        })
        TRACKS_LIST_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
            view = RecyclerViewItemView(RecyclerViewItemViewState(tracksLoadingInProgress), tracksAdapter, tracksItemDecoration, onTracksScrollListener)
            itemRecyclerView.layoutManager = LinearLayoutManager(parent.context, LinearLayoutManager.VERTICAL, false)
            itemRecyclerView.isNestedScrollingEnabled = false
        })
        else -> throw IllegalStateException("${javaClass.name}: Unknown viewType: $viewType")
    }

    override fun getItemCount(): Int = viewTypes.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = Unit

    companion object {
        private const val ARTISTS_HEADER_VIEW_TYPE = 0
        private const val ARTISTS_LIST_VIEW_TYPE = 1
        private const val TRACKS_HEADER_VIEW_TYPE = 2
        private const val TRACKS_LIST_VIEW_TYPE = 3

        private val viewTypes = arrayOf(
                ARTISTS_HEADER_VIEW_TYPE,
                ARTISTS_LIST_VIEW_TYPE,
                TRACKS_HEADER_VIEW_TYPE,
                TRACKS_LIST_VIEW_TYPE
        )
    }
}