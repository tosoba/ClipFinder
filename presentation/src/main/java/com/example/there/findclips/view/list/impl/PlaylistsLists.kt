package com.example.there.findclips.view.list.impl

import android.databinding.ObservableList
import com.example.there.findclips.databinding.GridPlaylistItemBinding
import com.example.there.findclips.databinding.PlaylistItemBinding
import com.example.there.findclips.model.entity.Playlist
import com.example.there.findclips.view.list.base.BaseBindingList
import com.example.there.findclips.view.list.vh.BaseBindingViewHolder

interface PlaylistsList {
    class Adapter(playlists: ObservableList<Playlist>, itemLayoutId: Int) :
            BaseBindingList.Adapter<Playlist, PlaylistItemBinding>(playlists, itemLayoutId) {

        override fun onBindViewHolder(holder: BaseBindingViewHolder<PlaylistItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.playlist = items[position]
        }
    }
}

interface GridPlaylistsList {
    class Adapter(playlists: ObservableList<Playlist>, itemLayoutId: Int) :
            BaseBindingList.Adapter<Playlist, GridPlaylistItemBinding>(playlists, itemLayoutId) {

        override fun onBindViewHolder(holder: BaseBindingViewHolder<GridPlaylistItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.playlist = items[position]
        }
    }
}
