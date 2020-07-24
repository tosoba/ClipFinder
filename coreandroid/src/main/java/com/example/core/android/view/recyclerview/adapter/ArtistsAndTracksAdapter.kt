package com.example.core.android.view.recyclerview.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coreandroid.R
import com.example.coreandroid.databinding.HeaderItemBinding
import com.example.coreandroid.databinding.RecyclerViewListItemBinding
import com.example.core.android.model.spotify.Artist
import com.example.core.android.model.spotify.Track
import com.example.core.android.util.ext.makeItemBinding
import com.example.core.android.view.recyclerview.BindingViewHolder
import com.example.core.android.view.recyclerview.item.RecyclerViewItemView

class ArtistsAndTracksAdapter(
    private val artistsRecyclerViewItemView: RecyclerViewItemView<Artist>,
    private val tracksRecyclerViewItemView: RecyclerViewItemView<Track>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int = viewTypes[position]

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RecyclerView.ViewHolder = when (viewType) {
        ARTISTS_HEADER_VIEW_TYPE -> BindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
            text = "Artists"
        })
        ARTISTS_LIST_VIEW_TYPE -> BindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
            view = artistsRecyclerViewItemView as RecyclerViewItemView<Any>
            itemRecyclerView.layoutManager = LinearLayoutManager(parent.context, LinearLayoutManager.HORIZONTAL, false)
        })
        TRACKS_HEADER_VIEW_TYPE -> BindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
            text = "Tracks"
        })
        TRACKS_LIST_VIEW_TYPE -> BindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
            view = tracksRecyclerViewItemView as RecyclerViewItemView<Any>
            itemRecyclerView.layoutManager = GridLayoutManager(parent.context, 2, GridLayoutManager.HORIZONTAL, false)
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
