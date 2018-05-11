package com.example.there.findclips.lists

import android.databinding.ObservableArrayList
import com.example.there.findclips.databinding.RelatedVideoItemBinding
import com.example.there.findclips.entities.Video

interface RelatedVideosList {
    class Adapter(videos: ObservableArrayList<Video>, itemLayoutId: Int, listener: RelatedVideosList.OnItemClickListener):
            BaseBindingList.Adapter<Video, RelatedVideoItemBinding>(videos, itemLayoutId, listener) {

        override fun onBindViewHolder(holder: BaseBindingList.ViewHolder<RelatedVideoItemBinding>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.video = items[position]
        }
    }

    interface OnItemClickListener : BaseBindingList.OnItemClickListener<Video>
}