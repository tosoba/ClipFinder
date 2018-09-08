package com.example.there.findclips.view.list

import android.databinding.ObservableList
import com.example.there.findclips.databinding.GridPlaylistItemBinding
import com.example.there.findclips.databinding.PlaylistItemBinding
import com.example.there.findclips.model.entity.Playlist

interface PlaylistsList {
    class Adapter(playlists: ObservableList<Playlist>, itemLayoutId: Int, listener: OnPlaylistClickListener) :
            BaseBindingList.Adapter<Playlist, PlaylistItemBinding>(playlists, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingList.ViewHolder<PlaylistItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.playlist = items[position]
        }
    }
}

interface GridPlaylistsList {
    class Adapter(playlists: ObservableList<Playlist>, itemLayoutId: Int, listener: OnPlaylistClickListener) :
            BaseBindingList.Adapter<Playlist, GridPlaylistItemBinding>(playlists, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingList.ViewHolder<GridPlaylistItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.playlist = items[position]
        }
    }
}

interface OnPlaylistClickListener : BaseBindingList.OnItemClickListener<Playlist>