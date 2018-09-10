package com.example.there.findclips.fragment.track

import android.databinding.ObservableField
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.databinding.AlbumInfoItemBinding
import com.example.there.findclips.databinding.HeaderItemBinding
import com.example.there.findclips.databinding.RecyclerViewListItemBinding
import com.example.there.findclips.util.ext.makeItemBinding
import com.example.there.findclips.view.list.item.AlbumInfoItemView
import com.example.there.findclips.view.list.item.HeaderItemViewState
import com.example.there.findclips.view.list.item.RecyclerViewItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemViewState
import com.example.there.findclips.view.list.vh.BaseBindingViewHolder

class TrackAdapter(
        private val albumInfoItemView: AlbumInfoItemView,
        private val artistsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
        private val similarTracksAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
        private val artistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        private val similarTracksLoadingInProgress: ObservableField<Boolean> = ObservableField(false)
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int = viewTypes[position]

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): RecyclerView.ViewHolder = when (viewType) {
        ALBUM_HEADER_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
            viewState = HeaderItemViewState("Album")
        })
        ALBUM_INFO_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<AlbumInfoItemBinding>(R.layout.album_info_item).apply {
            view = albumInfoItemView
        })
        ARTISTS_HEADER_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
            viewState = HeaderItemViewState("Artists")
        })
        ARTISTS_LIST_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
            view = RecyclerViewItemView(RecyclerViewItemViewState(artistsLoadingInProgress), artistsAdapter, null, null)
            itemRecyclerView.layoutManager = LinearLayoutManager(parent.context, LinearLayoutManager.HORIZONTAL, false)
            itemRecyclerView.isNestedScrollingEnabled = false
        })
        SIMILAR_TRACKS_HEADER_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
            viewState = HeaderItemViewState("Similar tracks")
        })
        SIMILAR_TRACKS_LIST_VIEW_TYPE -> BaseBindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
            view = RecyclerViewItemView(RecyclerViewItemViewState(similarTracksLoadingInProgress), similarTracksAdapter, null, null)
            itemRecyclerView.layoutManager = GridLayoutManager(parent.context, 2, GridLayoutManager.HORIZONTAL, false)
            itemRecyclerView.isNestedScrollingEnabled = false
        })
        else -> throw IllegalStateException("${javaClass.name}: Unknown viewType: $viewType")
    }

    override fun getItemCount(): Int = viewTypes.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = Unit

    companion object {
        private const val ALBUM_HEADER_VIEW_TYPE = 0
        private const val ALBUM_INFO_VIEW_TYPE = 1
        private const val ARTISTS_HEADER_VIEW_TYPE = 2
        private const val ARTISTS_LIST_VIEW_TYPE = 3
        private const val SIMILAR_TRACKS_HEADER_VIEW_TYPE = 4
        private const val SIMILAR_TRACKS_LIST_VIEW_TYPE = 5

        private val viewTypes = arrayOf(
                ALBUM_HEADER_VIEW_TYPE,
                ALBUM_INFO_VIEW_TYPE,
                ARTISTS_HEADER_VIEW_TYPE,
                ARTISTS_LIST_VIEW_TYPE,
                SIMILAR_TRACKS_HEADER_VIEW_TYPE,
                SIMILAR_TRACKS_LIST_VIEW_TYPE
        )
    }
}