package com.example.there.findclips.view.list.impl

import android.databinding.ObservableList
import android.support.v7.widget.RecyclerView
import com.example.there.findclips.databinding.GridPlaylistItemBinding
import com.example.there.findclips.databinding.PlaylistItemBinding
import com.example.there.findclips.model.entity.Playlist
import com.example.there.findclips.view.list.base.BaseBindingList
import com.example.there.findclips.view.list.vh.BaseBindingViewHolder

interface PlaylistsList {
    class Adapter(playlists: ObservableList<Playlist>, itemLayoutId: Int) :
            BaseBindingList.Adapter<Playlist, PlaylistItemBinding>(playlists, itemLayoutId) {

        @Suppress("UNCHECKED_CAST")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            (holder as BaseBindingViewHolder<PlaylistItemBinding>).binding.playlist = items[position]
        }
    }
}

interface GridPlaylistsList {
    class Adapter(playlists: ObservableList<Playlist>, itemLayoutId: Int) :
            BaseBindingList.Adapter<Playlist, GridPlaylistItemBinding>(playlists, itemLayoutId) {

        @Suppress("UNCHECKED_CAST")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            (holder as BaseBindingViewHolder<GridPlaylistItemBinding>).binding.playlist = items[position]
        }
    }
}
