package com.example.there.findclips.lists

import android.databinding.ObservableArrayList

import com.example.there.findclips.databinding.TopTrackItemBinding
import com.example.there.findclips.entities.TopTrack

interface TopTracksList {
    class Adapter(tracks: ObservableArrayList<TopTrack>, itemLayoutId: Int, listener: OnItemClickListener) :
            BaseBindingList.Adapter<TopTrack, TopTrackItemBinding>(tracks, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingList.ViewHolder<TopTrackItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.track = items[position]
        }
    }

    interface OnItemClickListener : BaseBindingList.OnItemClickListener<TopTrack>
}
