package com.example.there.findclips.fragment.search.videos

import android.databinding.ObservableField
import android.databinding.ObservableList
import com.example.there.findclips.model.entity.Video
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.view.list.item.RecyclerViewItemView

class VideosSearchView(
        val state: VideosSearchViewState,
        val recyclerViewItemView: RecyclerViewItemView<Video>
)

data class VideosSearchViewState(
        val videos: ObservableList<Video> = ObservableSortedList(Video::class.java, Video.sortedListCallback),
        val videosLoadingInProgress: ObservableField<Boolean> = ObservableField(false)
)