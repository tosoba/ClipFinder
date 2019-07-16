package com.example.spotifyartist

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.coreandroid.databinding.HeaderItemBinding
import com.example.coreandroid.databinding.RecyclerViewListItemBinding
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.Artist
import com.example.coreandroid.model.spotify.Track
import com.example.coreandroid.util.ext.makeItemBinding
import com.example.coreandroid.view.recyclerview.BindingViewHolder
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemView

class ArtistAdapter(
        private val albumsRecyclerViewItemView: RecyclerViewItemView<Album>,
        private val topTracksRecyclerViewItemView: RecyclerViewItemView<Track>,
        private val relatedArtitstsRecyclerViewItemView: RecyclerViewItemView<Artist>
) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int = viewTypes[position]

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): androidx.recyclerview.widget.RecyclerView.ViewHolder = when (viewType) {
        ALBUMS_HEADER_VIEW_TYPE -> BindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
            text = "Albums"
        })
        ALBUMS_LIST_VIEW_TYPE -> BindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
            view = albumsRecyclerViewItemView as RecyclerViewItemView<Any>
            itemRecyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(parent.context, 2, androidx.recyclerview.widget.GridLayoutManager.HORIZONTAL, false)
        })
        RELATED_ARTISTS_HEADER_VIEW_TYPE -> BindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
            text = "Related artists"
        })
        RELATED_ARTISTS_LIST_VIEW_TYPE -> BindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
            view = relatedArtitstsRecyclerViewItemView as RecyclerViewItemView<Any>
            itemRecyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(parent.context, 2, androidx.recyclerview.widget.GridLayoutManager.HORIZONTAL, false)
        })
        TOP_TRACKS_HEADER_VIEW_TYPE -> BindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
            text = "Top tracks"
        })
        TOP_TRACKS_LIST_VIEW_TYPE -> BindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
            view = topTracksRecyclerViewItemView as RecyclerViewItemView<Any>
            itemRecyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(parent.context, 2, androidx.recyclerview.widget.GridLayoutManager.HORIZONTAL, false)
        })
        else -> throw IllegalStateException("${javaClass.name}: Unknown viewType: $viewType")
    }

    override fun getItemCount(): Int = viewTypes.size

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) = Unit

    companion object {
        private const val ALBUMS_HEADER_VIEW_TYPE = 0
        private const val ALBUMS_LIST_VIEW_TYPE = 1
        private const val RELATED_ARTISTS_HEADER_VIEW_TYPE = 2
        private const val RELATED_ARTISTS_LIST_VIEW_TYPE = 3
        private const val TOP_TRACKS_HEADER_VIEW_TYPE = 4
        private const val TOP_TRACKS_LIST_VIEW_TYPE = 5

        private val viewTypes = arrayOf(
                ALBUMS_HEADER_VIEW_TYPE,
                ALBUMS_LIST_VIEW_TYPE,
                RELATED_ARTISTS_HEADER_VIEW_TYPE,
                RELATED_ARTISTS_LIST_VIEW_TYPE,
                TOP_TRACKS_HEADER_VIEW_TYPE,
                TOP_TRACKS_LIST_VIEW_TYPE
        )
    }
}