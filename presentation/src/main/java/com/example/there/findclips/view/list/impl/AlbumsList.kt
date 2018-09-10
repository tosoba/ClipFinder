package com.example.there.findclips.view.list.impl

import android.databinding.ObservableList
import com.example.there.findclips.databinding.AlbumItemBinding
import com.example.there.findclips.databinding.GridAlbumItemBinding
import com.example.there.findclips.model.entity.Album
import com.example.there.findclips.view.list.base.BaseBindingList
import com.example.there.findclips.view.list.vh.BaseBindingViewHolder

interface AlbumsList {
    class Adapter(albums: ObservableList<Album>, itemLayoutId: Int) :
            BaseBindingList.Adapter<Album, AlbumItemBinding>(albums, itemLayoutId) {

        override fun onBindViewHolder(holder: BaseBindingViewHolder<AlbumItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.album = items[position]
        }
    }
}

interface GridAlbumsList {
    class Adapter(albums: ObservableList<Album>, itemLayoutId: Int) :
            BaseBindingList.Adapter<Album, GridAlbumItemBinding>(albums, itemLayoutId) {

        override fun onBindViewHolder(holder: BaseBindingViewHolder<GridAlbumItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.album = items[position]
        }
    }
}

