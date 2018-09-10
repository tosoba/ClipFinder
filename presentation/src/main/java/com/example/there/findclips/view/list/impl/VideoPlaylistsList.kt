package com.example.there.findclips.view.list.impl

import android.databinding.ObservableList
import android.support.v7.widget.RecyclerView
import com.example.there.findclips.databinding.VideoPlaylistItemBinding
import com.example.there.findclips.model.entity.VideoPlaylist
import com.example.there.findclips.view.list.base.BaseBindingList
import com.example.there.findclips.view.list.vh.BaseBindingViewHolder

interface VideoPlaylistsList {
    class Adapter(playlists: ObservableList<VideoPlaylist>, itemLayoutId: Int) :
            BaseBindingList.Adapter<VideoPlaylist, VideoPlaylistItemBinding>(playlists, itemLayoutId) {

        @Suppress("UNCHECKED_CAST")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            (holder as BaseBindingViewHolder<VideoPlaylistItemBinding>).binding.playlist = items[position]
        }
    }
}
