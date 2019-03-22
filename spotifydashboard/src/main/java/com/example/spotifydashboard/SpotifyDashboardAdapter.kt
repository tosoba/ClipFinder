package com.example.spotifydashboard

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.Category
import com.example.coreandroid.model.spotify.Playlist
import com.example.coreandroid.model.spotify.TopTrack
import com.example.coreandroid.util.ext.makeItemBinding
import com.example.coreandroid.view.recyclerview.BaseBindingViewHolder
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemView
import com.example.coreandroid.R
import com.example.coreandroid.databinding.HeaderItemBinding
import com.example.coreandroid.databinding.RecyclerViewListItemBinding
import com.example.coreandroid.view.recyclerview.item.HeaderItemViewState

class SpotifyDashboardAdapter(
        private val categoriesRecyclerViewItemView: RecyclerViewItemView<Category>,
        private val playlistsRecyclerViewItemView: RecyclerViewItemView<Playlist>,
        private val tracksRecyclerViewItemView: RecyclerViewItemView<TopTrack>,
        private val albumsRecyclerViewItemView: RecyclerViewItemView<Album>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int = viewTypes[position]

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): RecyclerView.ViewHolder = when (viewType) {
        CATEGORIES_HEADER_VIEW_TYPE ->
            BaseBindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
                viewState = HeaderItemViewState("Genres")
            })
        CATEGORIES_LIST_VIEW_TYPE ->
            BaseBindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
                view = categoriesRecyclerViewItemView as RecyclerViewItemView<Any>
                itemRecyclerView.layoutManager = GridLayoutManager(parent.context, 2, GridLayoutManager.HORIZONTAL, false)
            })
        PLAYLISTS_HEADER_VIEW_TYPE ->
            BaseBindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
                viewState = HeaderItemViewState("Playlists")
            })
        PLAYLISTS_LIST_VIEW_TYPE ->
            BaseBindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
                view = playlistsRecyclerViewItemView as RecyclerViewItemView<Any>
                itemRecyclerView.layoutManager = GridLayoutManager(parent.context, 2, GridLayoutManager.HORIZONTAL, false)
            })
        ALBUMS_HEADER_VIEW_TYPE ->
            BaseBindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
                viewState = HeaderItemViewState("New releases")
            })
        ALBUMS_LIST_VIEW_TYPE ->
            BaseBindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
                view = albumsRecyclerViewItemView as RecyclerViewItemView<Any>
                itemRecyclerView.layoutManager = GridLayoutManager(parent.context, 2, GridLayoutManager.HORIZONTAL, false)
            })
        TRACKS_HEADER_VIEW_TYPE ->
            BaseBindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
                viewState = HeaderItemViewState("Top tracks")
            })
        TRACKS_LIST_VIEW_TYPE ->
            BaseBindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
                view = tracksRecyclerViewItemView as RecyclerViewItemView<Any>
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
        private const val ALBUMS_HEADER_VIEW_TYPE = 4
        private const val ALBUMS_LIST_VIEW_TYPE = 5
        private const val TRACKS_HEADER_VIEW_TYPE = 6
        private const val TRACKS_LIST_VIEW_TYPE = 7

        private val viewTypes = arrayOf(
                CATEGORIES_HEADER_VIEW_TYPE,
                CATEGORIES_LIST_VIEW_TYPE,
                PLAYLISTS_HEADER_VIEW_TYPE,
                PLAYLISTS_LIST_VIEW_TYPE,
                ALBUMS_HEADER_VIEW_TYPE,
                ALBUMS_LIST_VIEW_TYPE,
                TRACKS_HEADER_VIEW_TYPE,
                TRACKS_LIST_VIEW_TYPE
        )
    }
}