package com.example.there.findclips.view.lists

import android.databinding.ObservableArrayList
import com.example.there.findclips.databinding.AlbumItemBinding
import com.example.there.findclips.databinding.GridAlbumItemBinding
import com.example.there.findclips.model.entities.Album

interface AlbumsList {
    class Adapter(albums: ObservableArrayList<Album>, itemLayoutId: Int, listener: OnAlbumClickListener) :
            BaseBindingList.Adapter<Album, AlbumItemBinding>(albums, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingList.ViewHolder<AlbumItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.album = items[position]
        }
    }
}

interface GridAlbumsList {
    class Adapter(albums: ObservableArrayList<Album>, itemLayoutId: Int, listener: OnAlbumClickListener) :
            BaseBindingList.Adapter<Album, GridAlbumItemBinding>(albums, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingList.ViewHolder<GridAlbumItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.album = items[position]
        }
    }
}

interface OnAlbumClickListener : BaseBindingList.OnItemClickListener<Album>

