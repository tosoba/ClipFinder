package com.example.there.findclips.view.list

import android.databinding.ObservableList
import com.example.there.findclips.databinding.AlbumItemBinding
import com.example.there.findclips.databinding.GridAlbumItemBinding
import com.example.there.findclips.model.entity.Album

interface AlbumsList {
    class Adapter(albums: ObservableList<Album>, itemLayoutId: Int, listener: OnAlbumClickListener) :
            BaseBindingList.Adapter<Album, AlbumItemBinding>(albums, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingViewHolder<AlbumItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.album = items[position]
        }
    }
}

interface GridAlbumsList {
    class Adapter(albums: ObservableList<Album>, itemLayoutId: Int, listener: OnAlbumClickListener) :
            BaseBindingList.Adapter<Album, GridAlbumItemBinding>(albums, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingViewHolder<GridAlbumItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.album = items[position]
        }
    }
}

interface OnAlbumClickListener : OnItemClickListener<Album>

