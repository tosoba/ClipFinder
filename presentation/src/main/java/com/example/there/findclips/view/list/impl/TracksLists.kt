package com.example.there.findclips.view.list.impl

import android.databinding.ObservableList
import android.support.v7.widget.RecyclerView
import com.example.there.findclips.databinding.GridTrackItemBinding
import com.example.there.findclips.databinding.TopTrackItemBinding
import com.example.there.findclips.databinding.TrackItemBinding
import com.example.there.findclips.databinding.TrackPopularityItemBinding
import com.example.there.findclips.model.entity.TopTrack
import com.example.there.findclips.model.entity.Track
import com.example.there.findclips.view.list.base.BaseBindingList
import com.example.there.findclips.view.list.vh.BaseBindingViewHolder

interface GridTracksList {
    class Adapter(tracks: ObservableList<Track>, itemLayoutId: Int) :
            BaseBindingList.Adapter<Track, GridTrackItemBinding>(tracks, itemLayoutId) {

        @Suppress("UNCHECKED_CAST")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            (holder as BaseBindingViewHolder<GridTrackItemBinding>).binding.track = items[position]
        }
    }
}

interface TracksList {
    class Adapter(tracks: ObservableList<Track>, itemLayoutId: Int) :
            BaseBindingList.Adapter<Track, TrackItemBinding>(tracks, itemLayoutId) {

        @Suppress("UNCHECKED_CAST")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            (holder as BaseBindingViewHolder<TrackItemBinding>).binding.track = items[position]
        }
    }
}

interface TopTracksList {
    class Adapter(tracks: ObservableList<TopTrack>, itemLayoutId: Int) :
            BaseBindingList.Adapter<TopTrack, TopTrackItemBinding>(tracks, itemLayoutId) {

        @Suppress("UNCHECKED_CAST")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            (holder as BaseBindingViewHolder<TopTrackItemBinding>).binding.track = items[position]
        }
    }
}

interface TracksPopularityList {
    class Adapter(tracks: ObservableList<Track>, itemLayoutId: Int) :
            BaseBindingList.Adapter<Track, TrackPopularityItemBinding>(tracks, itemLayoutId) {

        @Suppress("UNCHECKED_CAST")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            (holder as BaseBindingViewHolder<TrackPopularityItemBinding>).binding.track = items[position]
        }
    }
}
