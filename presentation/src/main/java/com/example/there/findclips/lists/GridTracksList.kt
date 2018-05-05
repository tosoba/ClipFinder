package com.example.there.findclips.lists

import android.databinding.ObservableArrayList
import com.example.there.findclips.databinding.GridTrackItemBinding
import com.example.there.findclips.entities.Track

interface GridTracksList {
    class Adapter(tracks: ObservableArrayList<Track>, itemLayoutId: Int, listener: OnItemClickListener) :
            BaseBindingList.Adapter<Track, GridTrackItemBinding>(tracks, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingList.ViewHolder<GridTrackItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.track = items[position]
        }
    }

    interface OnItemClickListener : BaseBindingList.OnItemClickListener<Track>
}