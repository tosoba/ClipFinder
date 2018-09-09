package com.example.there.findclips.view.list

import android.databinding.ObservableList
import com.example.there.findclips.databinding.GridTrackItemBinding
import com.example.there.findclips.databinding.TopTrackItemBinding
import com.example.there.findclips.databinding.TrackItemBinding
import com.example.there.findclips.databinding.TrackPopularityItemBinding
import com.example.there.findclips.model.entity.TopTrack
import com.example.there.findclips.model.entity.Track

interface GridTracksList {
    class Adapter(tracks: ObservableList<Track>, itemLayoutId: Int, listener: OnTrackClickListener) :
            BaseBindingList.Adapter<Track, GridTrackItemBinding>(tracks, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingViewHolder<GridTrackItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.track = items[position]
        }
    }
}

interface TracksList {
    class Adapter(tracks: ObservableList<Track>, itemLayoutId: Int, listener: OnTrackClickListener) :
            BaseBindingList.Adapter<Track, TrackItemBinding>(tracks, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingViewHolder<TrackItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.track = items[position]
        }
    }
}

interface TopTracksList {
    class Adapter(tracks: ObservableList<TopTrack>, itemLayoutId: Int, listener: OnTopTrackClickListener) :
            BaseBindingList.Adapter<TopTrack, TopTrackItemBinding>(tracks, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingViewHolder<TopTrackItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.track = items[position]
        }
    }
}

interface TracksPopularityList {
    class Adapter(tracks: ObservableList<Track>, itemLayoutId: Int, listener: OnTrackClickListener) :
            BaseBindingList.Adapter<Track, TrackPopularityItemBinding>(tracks, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingViewHolder<TrackPopularityItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.track = items[position]
        }
    }
}

interface OnTrackClickListener : OnItemClickListener<Track>

interface OnTopTrackClickListener : OnItemClickListener<TopTrack>