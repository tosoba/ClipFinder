package com.example.there.findclips.view.list.impl

import android.databinding.ObservableField
import android.databinding.ObservableList
import android.support.v7.widget.RecyclerView
import com.example.there.findclips.databinding.AlbumItemBinding
import com.example.there.findclips.databinding.GridAlbumItemBinding
import com.example.there.findclips.model.entity.Album
import com.example.there.findclips.view.list.base.BaseBindingList
import com.example.there.findclips.view.list.base.BaseBindingLoadingList
import com.example.there.findclips.view.list.vh.BaseBindingViewHolder

interface AlbumsList {
    class Adapter(
            albums: ObservableList<Album>,
            itemLayoutId: Int,
            loadingMoreItemsInProgress: ObservableField<Boolean> = ObservableField(false)
    ) : BaseBindingLoadingList.Adapter<Album, AlbumItemBinding>(albums, itemLayoutId, loadingMoreItemsInProgress) {

        @Suppress("UNCHECKED_CAST")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            if (position < items.size)
                (holder as BaseBindingViewHolder<AlbumItemBinding>).binding.album = items[position]
        }
    }
}

interface GridAlbumsList {
    class Adapter(albums: ObservableList<Album>, itemLayoutId: Int) :
            BaseBindingList.Adapter<Album, GridAlbumItemBinding>(albums, itemLayoutId) {

        @Suppress("UNCHECKED_CAST")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            (holder as BaseBindingViewHolder<GridAlbumItemBinding>).binding.album = items[position]
        }
    }
}

