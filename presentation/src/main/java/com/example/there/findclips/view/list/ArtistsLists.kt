package com.example.there.findclips.view.list

import android.databinding.ObservableList
import com.example.there.findclips.databinding.ArtistItemBinding
import com.example.there.findclips.databinding.GridArtistItemBinding
import com.example.there.findclips.model.entity.Artist

interface ArtistsList {
    class Adapter(artists: ObservableList<Artist>, itemLayoutId: Int, listener: OnArtistClickListener) :
            BaseBindingList.Adapter<Artist, ArtistItemBinding>(artists, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingViewHolder<ArtistItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.artist = items[position]
        }
    }
}

interface GridArtistsList {
    class Adapter(artists: ObservableList<Artist>, itemLayoutId: Int, listener: OnArtistClickListener) :
            BaseBindingList.Adapter<Artist, GridArtistItemBinding>(artists, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingViewHolder<GridArtistItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.artist = items[position]
        }
    }
}

interface OnArtistClickListener : OnItemClickListener<Artist>