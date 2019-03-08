package com.example.there.findclips.soundcloud.dashboard

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.databinding.HeaderItemBinding
import com.example.there.findclips.databinding.RecyclerViewListItemBinding
import com.example.there.findclips.model.entity.soundcloud.SoundCloudPlaylist
import com.example.there.findclips.model.entity.soundcloud.SoundCloudSystemPlaylist
import com.example.there.findclips.util.ext.makeItemBinding
import com.example.there.findclips.view.list.BaseBindingViewHolder
import com.example.there.findclips.view.list.item.HeaderItemViewState
import com.example.there.findclips.view.list.item.RecyclerViewItemView

class SoundCloudDashboardAdapter(
        private val playlistsRecyclerItemView: RecyclerViewItemView<SoundCloudPlaylist>,
        private val systemPlaylistsRecyclerItemView: RecyclerViewItemView<SoundCloudSystemPlaylist>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int = viewTypes[position]

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): RecyclerView.ViewHolder = when (viewType) {
        PLAYLISTS_HEADER_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
            viewState = HeaderItemViewState("Discover")
        })
        PLAYLISTS_LIST_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
            view = playlistsRecyclerItemView as RecyclerViewItemView<Any>
            itemRecyclerView.layoutManager = GridLayoutManager(parent.context, 2, GridLayoutManager.HORIZONTAL, false)
        })
        SYSTEM_PLAYLIST_HEADER_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
            viewState = HeaderItemViewState("Top & Trending")
        })
        SYSTEM_PLAYLIST_LIST_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
            view = systemPlaylistsRecyclerItemView as RecyclerViewItemView<Any>
            itemRecyclerView.layoutManager = GridLayoutManager(parent.context, 2, GridLayoutManager.HORIZONTAL, false)
        })
        else -> throw IllegalStateException("${javaClass.name}: Unknown viewType: $viewType")
    }

    override fun getItemCount(): Int = viewTypes.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = Unit

    companion object {
        private const val PLAYLISTS_HEADER_VIEW_TYPE = 0
        private const val PLAYLISTS_LIST_VIEW_TYPE = 1
        private const val SYSTEM_PLAYLIST_HEADER_VIEW_TYPE = 2
        private const val SYSTEM_PLAYLIST_LIST_VIEW_TYPE = 3

        private val viewTypes = arrayOf(
                PLAYLISTS_HEADER_VIEW_TYPE,
                PLAYLISTS_LIST_VIEW_TYPE,
                SYSTEM_PLAYLIST_HEADER_VIEW_TYPE,
                SYSTEM_PLAYLIST_LIST_VIEW_TYPE
        )
    }
}