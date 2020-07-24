package com.example.core.android.view.recyclerview.item

import android.view.View
import com.example.core.android.model.videos.Video
import com.example.core.android.util.list.ObservableSortedList

class VideoItemView(
    val video: Video,
    val onRemoveBtnClickListener: View.OnClickListener? = null
) {
    companion object {
        val sortedListCallback: ObservableSortedList.Callback<VideoItemView> = object : ObservableSortedList.Callback<VideoItemView> {
            override fun compare(o1: VideoItemView, o2: VideoItemView): Int = -1
            override fun areItemsTheSame(item1: VideoItemView, item2: VideoItemView): Boolean = item1.video.id == item2.video.id
            override fun areContentsTheSame(oldItem: VideoItemView, newItem: VideoItemView): Boolean = oldItem.video.id == newItem.video.id
        }
    }
}
