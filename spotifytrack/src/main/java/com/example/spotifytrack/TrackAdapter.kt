package com.example.spotifytrack

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coreandroid.databinding.AlbumInfoItemBinding
import com.example.coreandroid.databinding.HeaderItemBinding
import com.example.coreandroid.databinding.RadarChartBinding
import com.example.coreandroid.databinding.RecyclerViewListItemBinding
import com.example.coreandroid.model.spotify.Artist
import com.example.coreandroid.model.spotify.Track
import com.example.coreandroid.util.ext.makeItemBinding
import com.example.coreandroid.view.radarchart.RadarChartView
import com.example.coreandroid.view.recyclerview.BindingViewHolder
import com.example.coreandroid.view.recyclerview.item.AlbumInfoItemView
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemView

class TrackAdapter(
        private val albumInfoItemView: AlbumInfoItemView,
        private val artistsRecyclerViewItemView: RecyclerViewItemView<Artist>,
        private val similarTracksRecyclerViewItemView: RecyclerViewItemView<Track>,
        private val audioFeaturesChartView: RadarChartView
) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int = viewTypes[position]

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): androidx.recyclerview.widget.RecyclerView.ViewHolder = when (viewType) {
        ALBUM_HEADER_VIEW_TYPE -> BindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
            text = "Album"
        })
        ALBUM_INFO_VIEW_TYPE -> BindingViewHolder(parent.makeItemBinding<AlbumInfoItemBinding>(R.layout.album_info_item).apply {
            view = albumInfoItemView
        })
        ARTISTS_HEADER_VIEW_TYPE -> BindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
            text = "Artists"
        })
        ARTISTS_LIST_VIEW_TYPE -> BindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
            view = artistsRecyclerViewItemView as RecyclerViewItemView<Any>
            itemRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(parent.context, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        })
        SIMILAR_TRACKS_HEADER_VIEW_TYPE -> BindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
            text = "Similar tracks"
        })
        SIMILAR_TRACKS_LIST_VIEW_TYPE -> BindingViewHolder(parent.makeItemBinding<RecyclerViewListItemBinding>(R.layout.recycler_view_list_item).apply {
            view = similarTracksRecyclerViewItemView as RecyclerViewItemView<Any>
            itemRecyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(parent.context, 2, androidx.recyclerview.widget.GridLayoutManager.HORIZONTAL, false)
        })
        AUDIO_FEATURES_HEADER_VIEW_TYPE -> BindingViewHolder(parent.makeItemBinding<HeaderItemBinding>(R.layout.header_item).apply {
            text = "Audio features"
        })
        AUDIO_FEATURES_CHART_VIEW_TYPE -> BindingViewHolder(parent.makeItemBinding<RadarChartBinding>(R.layout.radar_chart).apply {
            view = audioFeaturesChartView
        })
        else -> throw IllegalStateException("${javaClass.name}: Unknown viewType: $viewType")
    }

    override fun getItemCount(): Int = viewTypes.size

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) = Unit

    companion object {
        private const val ALBUM_HEADER_VIEW_TYPE = 0
        private const val ALBUM_INFO_VIEW_TYPE = 1
        private const val ARTISTS_HEADER_VIEW_TYPE = 2
        private const val ARTISTS_LIST_VIEW_TYPE = 3
        private const val SIMILAR_TRACKS_HEADER_VIEW_TYPE = 4
        private const val SIMILAR_TRACKS_LIST_VIEW_TYPE = 5
        private const val AUDIO_FEATURES_HEADER_VIEW_TYPE = 6
        private const val AUDIO_FEATURES_CHART_VIEW_TYPE = 7

        private val viewTypes = arrayOf(
                ALBUM_HEADER_VIEW_TYPE,
                ALBUM_INFO_VIEW_TYPE,
                ARTISTS_HEADER_VIEW_TYPE,
                ARTISTS_LIST_VIEW_TYPE,
                SIMILAR_TRACKS_HEADER_VIEW_TYPE,
                SIMILAR_TRACKS_LIST_VIEW_TYPE,
                AUDIO_FEATURES_HEADER_VIEW_TYPE,
                AUDIO_FEATURES_CHART_VIEW_TYPE
        )
    }
}