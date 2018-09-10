package com.example.there.findclips.fragment.dashboard

import android.databinding.ObservableField
import android.support.v7.widget.GridLayoutManager
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

class DashboardAdapter(
        private val categoriesAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
        private val playlistsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
        private val tracksAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
        private val categoriesLoadingInProgress: ObservableField<Boolean>,
        private val playlistsLoadingInProgress: ObservableField<Boolean>,
        private val tracksLoadingInProgress: ObservableField<Boolean>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int = viewTypes[position]

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): RecyclerView.ViewHolder = when (viewType) {
        CATEGORIES_HEADER_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
            viewState = HeaderItemViewState("Genres")
        })
        CATEGORIES_LIST_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
            view = RecyclerViewItemView(RecyclerViewItemViewState(categoriesLoadingInProgress), categoriesAdapter, null, null)
            itemRecyclerView.layoutManager = GridLayoutManager(parent.context, 2, GridLayoutManager.HORIZONTAL, false)
        })
        PLAYLISTS_HEADER_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
            viewState = HeaderItemViewState("Playlists")
        })
        PLAYLISTS_LIST_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
            view = RecyclerViewItemView(RecyclerViewItemViewState(playlistsLoadingInProgress), playlistsAdapter, null, null)
            itemRecyclerView.layoutManager = GridLayoutManager(parent.context, 2, GridLayoutManager.HORIZONTAL, false)
        })
        TRACKS_HEADER_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
            viewState = HeaderItemViewState("Top tracks")
        })
        TRACKS_LIST_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
            view = RecyclerViewItemView(RecyclerViewItemViewState(tracksLoadingInProgress), tracksAdapter, null, null)
            itemRecyclerView.layoutManager = LinearLayoutManager(parent.context, LinearLayoutManager.HORIZONTAL, false)
        })
        else -> throw IllegalStateException("${javaClass.name}: Unknown viewType: $viewType")
    }

    override fun getItemCount(): Int = viewTypes.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = Unit

    companion object {
        private const val CATEGORIES_HEADER_VIEW_TYPE = 0
        private const val CATEGORIES_LIST_VIEW_TYPE = 1
        private const val PLAYLISTS_HEADER_VIEW_TYPE = 2
        private const val PLAYLISTS_LIST_VIEW_TYPE = 3
        private const val TRACKS_HEADER_VIEW_TYPE = 4
        private const val TRACKS_LIST_VIEW_TYPE = 5

        private val viewTypes = arrayOf(
                CATEGORIES_HEADER_VIEW_TYPE,
                CATEGORIES_LIST_VIEW_TYPE,
                PLAYLISTS_HEADER_VIEW_TYPE,
                PLAYLISTS_LIST_VIEW_TYPE,
                TRACKS_HEADER_VIEW_TYPE,
                TRACKS_LIST_VIEW_TYPE
        )
    }
}