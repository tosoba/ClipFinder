package com.example.there.findclips.lists

import android.databinding.ObservableArrayList
import com.example.there.findclips.databinding.GridAlbumItemBinding
import com.example.there.findclips.entities.Album


interface GridAlbumsList {
    class Adapter(albums: ObservableArrayList<Album>, itemLayoutId: Int, listener: OnItemClickListener) :
            BaseBindingList.Adapter<Album, GridAlbumItemBinding>(albums, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingList.ViewHolder<GridAlbumItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.album = items[position]
        }
    }

    interface OnItemClickListener : BaseBindingList.OnItemClickListener<Album>
}