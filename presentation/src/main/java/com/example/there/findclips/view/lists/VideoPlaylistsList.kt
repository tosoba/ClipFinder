package com.example.there.findclips.view.lists

import android.databinding.ObservableArrayList
import com.example.there.findclips.databinding.VideoPlaylistItemBinding
import com.example.there.findclips.model.entities.VideoPlaylist

interface VideoPlaylistsList {
    class Adapter(playlists: ObservableArrayList<VideoPlaylist>, itemLayoutId: Int, listener: OnVideoPlaylistClickListener) :
            BaseBindingList.Adapter<VideoPlaylist, VideoPlaylistItemBinding>(playlists, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingList.ViewHolder<VideoPlaylistItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.playlist = items[position]
        }
    }
}

interface OnVideoPlaylistClickListener : BaseBindingList.OnItemClickListener<VideoPlaylist>