package com.example.there.findclips.view.list.impl

import android.databinding.ObservableList
import com.example.there.findclips.databinding.RelatedVideoItemBinding
import com.example.there.findclips.databinding.VideoItemBinding
import com.example.there.findclips.model.entity.Video
import com.example.there.findclips.view.list.base.BaseBindingList
import com.example.there.findclips.view.list.vh.BaseBindingViewHolder

interface VideosList {
    class Adapter(videos: ObservableList<Video>, itemLayoutId: Int) :
            BaseBindingList.Adapter<Video, VideoItemBinding>(videos, itemLayoutId) {

        override fun onBindViewHolder(holder: BaseBindingViewHolder<VideoItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.video = items[position]
        }
    }
}

interface RelatedVideosList {
    class Adapter(videos: ObservableList<Video>, itemLayoutId: Int):
            BaseBindingList.Adapter<Video, RelatedVideoItemBinding>(videos, itemLayoutId) {

        override fun onBindViewHolder(holder: BaseBindingViewHolder<RelatedVideoItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.video = items[position]
        }
    }
}
