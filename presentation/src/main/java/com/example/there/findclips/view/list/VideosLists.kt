package com.example.there.findclips.view.list

import android.databinding.ObservableList
import com.example.there.findclips.databinding.RelatedVideoItemBinding
import com.example.there.findclips.databinding.VideoItemBinding
import com.example.there.findclips.model.entity.Video

interface VideosList {
    class Adapter(videos: ObservableList<Video>, itemLayoutId: Int, listener: OnVideoClickListener) :
            BaseBindingList.Adapter<Video, VideoItemBinding>(videos, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingViewHolder<VideoItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.video = items[position]
        }
    }
}

interface RelatedVideosList {
    class Adapter(videos: ObservableList<Video>, itemLayoutId: Int, listener: OnVideoClickListener):
            BaseBindingList.Adapter<Video, RelatedVideoItemBinding>(videos, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingViewHolder<RelatedVideoItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.video = items[position]
        }
    }
}

interface OnVideoClickListener : OnItemClickListener<Video>