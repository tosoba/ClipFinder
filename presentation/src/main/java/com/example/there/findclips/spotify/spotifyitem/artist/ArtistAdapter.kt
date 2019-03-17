package com.example.there.findclips.spotify.spotifyitem.artist

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.there.findclips.R

class ArtistAdapter(
        private val albumsRecyclerViewItemView: RecyclerViewItemView<Album>,
        private val topTracksRecyclerViewItemView: RecyclerViewItemView<Track>,
        private val relatedArtitstsRecyclerViewItemView: RecyclerViewItemView<Artist>
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
        RELATED_ARTISTS_HEADER_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
            viewState = HeaderItemViewState("Related artists")
        })
        RELATED_ARTISTS_LIST_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
            view = relatedArtitstsRecyclerViewItemView as RecyclerViewItemView<Any>
            itemRecyclerView.layoutManager = GridLayoutManager(parent.context, 2, GridLayoutManager.HORIZONTAL, false)
        })
        TOP_TRACKS_HEADER_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
            viewState = HeaderItemViewState("Top tracks")
        })
        TOP_TRACKS_LIST_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
            view = topTracksRecyclerViewItemView as RecyclerViewItemView<Any>
            itemRecyclerView.layoutManager = GridLayoutManager(parent.context, 2, GridLayoutManager.HORIZONTAL, false)
        })
        else -> throw IllegalStateException("${javaClass.name}: Unknown viewType: $viewType")
    }

    override fun getItemCount(): Int = viewTypes.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = Unit

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