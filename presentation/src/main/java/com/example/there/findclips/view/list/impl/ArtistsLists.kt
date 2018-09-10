package com.example.there.findclips.view.list.impl

import android.databinding.ObservableList
import com.example.there.findclips.databinding.ArtistItemBinding
import com.example.there.findclips.databinding.GridArtistItemBinding
import com.example.there.findclips.model.entity.Artist
import com.example.there.findclips.view.list.base.BaseBindingList
import com.example.there.findclips.view.list.vh.BaseBindingViewHolder

interface ArtistsList {
    class Adapter(artists: ObservableList<Artist>, itemLayoutId: Int) :
            BaseBindingList.Adapter<Artist, ArtistItemBinding>(artists, itemLayoutId) {

        override fun onBindViewHolder(holder: BaseBindingViewHolder<ArtistItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.artist = items[position]
        }
    }
}

interface GridArtistsList {
    class Adapter(artists: ObservableList<Artist>, itemLayoutId: Int) :
            BaseBindingList.Adapter<Artist, GridArtistItemBinding>(artists, itemLayoutId) {

        override fun onBindViewHolder(holder: BaseBindingViewHolder<GridArtistItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.artist = items[position]
        }
    }
}
