package com.example.there.findclips.lists

import android.databinding.ObservableArrayList
import com.example.there.domain.entities.spotify.PlaylistEntity
import com.example.there.findclips.databinding.PlaylistItemBinding

interface PlaylistsList {
    class Adapter(playlists: ObservableArrayList<PlaylistEntity>, itemLayoutId: Int, listener: OnItemClickListener) :
            BaseBindingList.Adapter<PlaylistEntity, PlaylistItemBinding>(playlists, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingList.ViewHolder<PlaylistItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.playlist = items[position]
        }
    }

    interface OnItemClickListener : BaseBindingList.OnItemClickListener<PlaylistEntity>
}
