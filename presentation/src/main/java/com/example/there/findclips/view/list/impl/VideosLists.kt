package com.example.there.findclips.view.list.impl

import android.databinding.ObservableField
import android.databinding.ObservableList
import android.support.v7.widget.RecyclerView
import com.example.there.findclips.databinding.RelatedVideoItemBinding
import com.example.there.findclips.databinding.VideoItemBinding
import com.example.there.findclips.model.entity.Video
import com.example.there.findclips.view.list.base.BaseBindingList
import com.example.there.findclips.view.list.base.BaseBindingLoadingList
import com.example.there.findclips.view.list.base.TracksInitialScroll
import com.example.there.findclips.view.list.vh.BaseBindingViewHolder

interface VideosList {
    class Adapter(videos: ObservableList<Video>, itemLayoutId: Int) :
            BaseBindingList.Adapter<Video, VideoItemBinding>(videos, itemLayoutId) {

        @Suppress("UNCHECKED_CAST")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            (holder as BaseBindingViewHolder<VideoItemBinding>).binding.video = items[position]
        }
    }
}

interface RelatedVideosList {
    class Adapter(
            videos: ObservableList<Video>,
            itemLayoutId: Int,
            loadingMoreItemsInProgress: ObservableField<Boolean>
    ) : BaseBindingLoadingList.Adapter<Video, RelatedVideoItemBinding>(videos, itemLayoutId, loadingMoreItemsInProgress),
            TracksInitialScroll {

        override var userHasScrolled: Boolean = false

        @Suppress("UNCHECKED_CAST")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            if (position < items.size)
                (holder as BaseBindingViewHolder<RelatedVideoItemBinding>).binding.video = items[position]
        }
    }
}
