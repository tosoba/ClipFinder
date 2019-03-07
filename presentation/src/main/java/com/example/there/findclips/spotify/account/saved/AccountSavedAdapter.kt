package com.example.there.findclips.spotify.account.saved

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.databinding.HeaderItemBinding
import com.example.there.findclips.databinding.RecyclerViewListItemBinding
import com.example.there.findclips.model.entity.Album
import com.example.there.findclips.model.entity.Track
import com.example.there.findclips.util.ext.makeItemBinding
import com.example.there.findclips.view.list.BaseBindingViewHolder
import com.example.there.findclips.view.list.item.HeaderItemViewState
import com.example.there.findclips.view.list.item.RecyclerViewItemView

class AccountSavedAdapter(
        private val albumsRecyclerViewItemView: RecyclerViewItemView<Album>,
        private val tracksRecyclerViewItemView: RecyclerViewItemView<Track>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int = viewTypes[position]

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): RecyclerView.ViewHolder = when (viewType) {
        ALBUMS_HEADER_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
            viewState = HeaderItemViewState("Albums")
        })
        ALBUMS_LIST_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
            view = albumsRecyclerViewItemView as RecyclerViewItemView<Any>
            itemRecyclerView.layoutManager = GridLayoutManager(parent.context, 2, GridLayoutManager.HORIZONTAL, false)
        })
        TRACKS_HEADER_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
            viewState = HeaderItemViewState("Tracks")
        })
        TRACKS_LIST_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
            view = tracksRecyclerViewItemView as RecyclerViewItemView<Any>
            itemRecyclerView.layoutManager = GridLayoutManager(parent.context, 2, GridLayoutManager.HORIZONTAL, false)
        })
        else -> throw IllegalStateException("${javaClass.name}: Unknown viewType: $viewType")
    }

    override fun getItemCount(): Int = viewTypes.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = Unit

    companion object {
        private const val ALBUMS_HEADER_VIEW_TYPE = 0
        private const val ALBUMS_LIST_VIEW_TYPE = 1
        private const val TRACKS_HEADER_VIEW_TYPE = 2
        private const val TRACKS_LIST_VIEW_TYPE = 3

        private val viewTypes = arrayOf(
                ALBUMS_HEADER_VIEW_TYPE,
                ALBUMS_LIST_VIEW_TYPE,
                TRACKS_HEADER_VIEW_TYPE,
                TRACKS_LIST_VIEW_TYPE
        )
    }
}
