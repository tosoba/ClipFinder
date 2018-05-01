package com.example.there.findclips.lists

import android.databinding.ObservableArrayList
import com.example.there.domain.entities.spotify.TopTrackEntity
import com.example.there.findclips.databinding.TopTrackItemBinding

interface TopTracksList {
    class Adapter(tracks: ObservableArrayList<TopTrackEntity>, itemLayoutId: Int, listener: OnItemClickListener) :
            BaseBindingList.Adapter<TopTrackEntity, TopTrackItemBinding>(tracks, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingList.ViewHolder<TopTrackItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.track = items[position]
        }
    }

    interface OnItemClickListener : BaseBindingList.OnItemClickListener<TopTrackEntity>
}
