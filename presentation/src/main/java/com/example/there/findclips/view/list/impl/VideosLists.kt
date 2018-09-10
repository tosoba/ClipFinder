package com.example.there.findclips.view.list.impl

import android.databinding.ObservableList
import android.support.v7.widget.RecyclerView
import com.example.there.findclips.databinding.RelatedVideoItemBinding
import com.example.there.findclips.databinding.VideoItemBinding
import com.example.there.findclips.model.entity.Video
import com.example.there.findclips.view.list.base.BaseBindingList
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
    class Adapter(videos: ObservableList<Video>, itemLayoutId: Int) :
            BaseBindingList.Adapter<Video, RelatedVideoItemBinding>(videos, itemLayoutId) {

        @Suppress("UNCHECKED_CAST")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            (holder as BaseBindingViewHolder<RelatedVideoItemBinding>).binding.video = items[position]
        }
    }
}
