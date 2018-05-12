package com.example.there.findclips.lists

import android.databinding.ObservableArrayList
import com.example.there.findclips.databinding.GridPlaylistItemBinding
import com.example.there.findclips.databinding.PlaylistItemBinding
import com.example.there.findclips.entities.Playlist

interface PlaylistsList {
    class Adapter(playlists: ObservableArrayList<Playlist>, itemLayoutId: Int, listener: OnItemClickListener) :
            BaseBindingList.Adapter<Playlist, PlaylistItemBinding>(playlists, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingList.ViewHolder<PlaylistItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.playlist = items[position]
        }
    }

    interface OnItemClickListener : BaseBindingList.OnItemClickListener<Playlist>
}

interface GridPlaylistsList {
    class Adapter(playlists: ObservableArrayList<Playlist>, itemLayoutId: Int, listener: OnItemClickListener) :
            BaseBindingList.Adapter<Playlist, GridPlaylistItemBinding>(playlists, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingList.ViewHolder<GridPlaylistItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.playlist = items[position]
        }
    }

    interface OnItemClickListener : BaseBindingList.OnItemClickListener<Playlist>
}