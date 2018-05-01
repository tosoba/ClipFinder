package com.example.there.findclips.lists

import android.databinding.ObservableArrayList
import com.example.there.findclips.databinding.VideoItemBinding
import com.example.there.findclips.entities.Video

interface VideosList {

    class Adapter(videos: ObservableArrayList<Video>, itemLayoutId: Int, listener: OnItemClickListener) :
            BaseBindingList.Adapter<Video, VideoItemBinding>(videos, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingList.ViewHolder<VideoItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.video = items[position]
        }
    }

    interface OnItemClickListener : BaseBindingList.OnItemClickListener<Video>
}
