package com.example.there.findclips.view.list

import android.databinding.ObservableList
import com.example.there.findclips.databinding.VideoPlaylistItemBinding
import com.example.there.findclips.model.entity.VideoPlaylist

interface VideoPlaylistsList {
    class Adapter(playlists: ObservableList<VideoPlaylist>, itemLayoutId: Int, listener: OnVideoPlaylistClickListener) :
            BaseBindingList.Adapter<VideoPlaylist, VideoPlaylistItemBinding>(playlists, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingList.ViewHolder<VideoPlaylistItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.playlist = items[position]
        }
    }
}

interface OnVideoPlaylistClickListener : BaseBindingList.OnItemClickListener<VideoPlaylist>