package com.example.spotifydashboard

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coreandroid.R
import com.example.coreandroid.databinding.HeaderItemBinding
import com.example.coreandroid.databinding.RecyclerViewListItemBinding
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.Category
import com.example.coreandroid.model.spotify.Playlist
import com.example.coreandroid.model.spotify.TopTrack
import com.example.coreandroid.util.ext.makeItemBinding
import com.example.coreandroid.view.recyclerview.BindingViewHolder
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemView

class SpotifyDashboardAdapter(
        private val categoriesRecyclerViewItemView: RecyclerViewItemView<Category>,
        private val playlistsRecyclerViewItemView: RecyclerViewItemView<Playlist>,
        private val tracksRecyclerViewItemView: RecyclerViewItemView<TopTrack>,
        private val albumsRecyclerViewItemView: RecyclerViewItemView<Album>
) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int = viewTypes[position]

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): androidx.recyclerview.widget.RecyclerView.ViewHolder = when (viewType) {
        CATEGORIES_HEADER_VIEW_TYPE ->
            BindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
                text = "Genres"
            })
        CATEGORIES_LIST_VIEW_TYPE ->
            BindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
                view = categoriesRecyclerViewItemView as RecyclerViewItemView<Any>
                itemRecyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(parent.context, 2, androidx.recyclerview.widget.GridLayoutManager.HORIZONTAL, false)
            })
        PLAYLISTS_HEADER_VIEW_TYPE ->
            BindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
                text = "Playlists"
            })
        PLAYLISTS_LIST_VIEW_TYPE ->
            BindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
                view = playlistsRecyclerViewItemView as RecyclerViewItemView<Any>
                itemRecyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(parent.context, 2, androidx.recyclerview.widget.GridLayoutManager.HORIZONTAL, false)
            })
        ALBUMS_HEADER_VIEW_TYPE ->
            BindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
                text = "New releases"
            })
        ALBUMS_LIST_VIEW_TYPE ->
            BindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
                view = albumsRecyclerViewItemView as RecyclerViewItemView<Any>
                itemRecyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(parent.context, 2, androidx.recyclerview.widget.GridLayoutManager.HORIZONTAL, false)
            })
        TRACKS_HEADER_VIEW_TYPE ->
            BindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
                text = "Top tracks"
            })
        TRACKS_LIST_VIEW_TYPE ->
            BindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
                view = tracksRecyclerViewItemView as RecyclerViewItemView<Any>
                itemRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(parent.context, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
            })
        else -> throw IllegalStateException("${javaClass.name}: Unknown viewType: $viewType")
    }

    override fun getItemCount(): Int = viewTypes.size

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) = Unit

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