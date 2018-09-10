package com.example.there.findclips.view.list.impl

import android.databinding.ObservableList
import com.example.there.findclips.databinding.VideoPlaylistItemBinding
import com.example.there.findclips.model.entity.VideoPlaylist
import com.example.there.findclips.view.list.base.BaseBindingList
import com.example.there.findclips.view.list.vh.BaseBindingViewHolder

interface VideoPlaylistsList {
    class Adapter(playlists: ObservableList<VideoPlaylist>, itemLayoutId: Int) :
            BaseBindingList.Adapter<VideoPlaylist, VideoPlaylistItemBinding>(playlists, itemLayoutId) {

        override fun onBindViewHolder(holder: BaseBindingViewHolder<VideoPlaylistItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.playlist = items[position]
        }
    }
}
