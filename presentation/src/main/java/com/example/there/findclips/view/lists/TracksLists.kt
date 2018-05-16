package com.example.there.findclips.view.lists

import android.databinding.ObservableArrayList
import com.example.there.findclips.databinding.GridTrackItemBinding
import com.example.there.findclips.databinding.TrackItemBinding
import com.example.there.findclips.databinding.TopTrackItemBinding
import com.example.there.findclips.model.entities.TopTrack
import com.example.there.findclips.model.entities.Track

interface GridTracksList {
    class Adapter(tracks: ObservableArrayList<Track>, itemLayoutId: Int, listener: OnTrackClickListener) :
            BaseBindingList.Adapter<Track, GridTrackItemBinding>(tracks, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingList.ViewHolder<GridTrackItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.track = items[position]
        }
    }
}

interface TracksList {
    class Adapter(tracks: ObservableArrayList<Track>, itemLayoutId: Int, listener: OnTrackClickListener) :
            BaseBindingList.Adapter<Track, TrackItemBinding>(tracks, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingList.ViewHolder<TrackItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.track = items[position]
        }
    }
}

interface TopTracksList {
    class Adapter(tracks: ObservableArrayList<TopTrack>, itemLayoutId: Int, listener: OnTopTrackClickListener) :
            BaseBindingList.Adapter<TopTrack, TopTrackItemBinding>(tracks, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingList.ViewHolder<TopTrackItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.track = items[position]
        }
    }
}

interface OnTrackClickListener : BaseBindingList.OnItemClickListener<Track>

interface OnTopTrackClickListener : BaseBindingList.OnItemClickListener<TopTrack>