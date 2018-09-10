package com.example.there.findclips.view.list.impl

import android.databinding.ObservableList
import android.support.v7.widget.RecyclerView
import com.example.there.findclips.databinding.ArtistItemBinding
import com.example.there.findclips.databinding.GridArtistItemBinding
import com.example.there.findclips.model.entity.Artist
import com.example.there.findclips.view.list.base.BaseBindingList
import com.example.there.findclips.view.list.vh.BaseBindingViewHolder

interface ArtistsList {
    class Adapter(artists: ObservableList<Artist>, itemLayoutId: Int) :
            BaseBindingList.Adapter<Artist, ArtistItemBinding>(artists, itemLayoutId) {

        @Suppress("UNCHECKED_CAST")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            (holder as BaseBindingViewHolder<ArtistItemBinding>).binding.artist = items[position]
        }
    }
}

interface GridArtistsList {
    class Adapter(artists: ObservableList<Artist>, itemLayoutId: Int) :
            BaseBindingList.Adapter<Artist, GridArtistItemBinding>(artists, itemLayoutId) {

        @Suppress("UNCHECKED_CAST")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            (holder as BaseBindingViewHolder<GridArtistItemBinding>).binding.artist = items[position]
        }
    }
}
