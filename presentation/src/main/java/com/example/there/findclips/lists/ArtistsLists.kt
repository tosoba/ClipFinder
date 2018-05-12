package com.example.there.findclips.lists

import android.databinding.ObservableArrayList
import com.example.there.findclips.databinding.ArtistItemBinding
import com.example.there.findclips.databinding.GridArtistItemBinding
import com.example.there.findclips.entities.Artist

interface ArtistsList {

    class Adapter(artists: ObservableArrayList<Artist>, itemLayoutId: Int, listener: OnItemClickListener) :
            BaseBindingList.Adapter<Artist, ArtistItemBinding>(artists, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingList.ViewHolder<ArtistItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.artist = items[position]
        }
    }

    interface OnItemClickListener : BaseBindingList.OnItemClickListener<Artist>
}

interface GridArtistsList {
    class Adapter(artists: ObservableArrayList<Artist>, itemLayoutId: Int, listener: OnItemClickListener) :
            BaseBindingList.Adapter<Artist, GridArtistItemBinding>(artists, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingList.ViewHolder<GridArtistItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.artist = items[position]
        }
    }

    interface OnItemClickListener : BaseBindingList.OnItemClickListener<Artist>
}