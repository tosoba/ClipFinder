package com.example.there.findclips.fragment.search.videos

import android.databinding.ObservableField
import android.databinding.ObservableList
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.view.list.item.RecyclerViewItemView
import com.example.there.findclips.view.list.item.VideoItemView

class VideosSearchView(
        val state: VideosSearchViewState,
        val recyclerViewItemView: RecyclerViewItemView<VideoItemView>
)

data class VideosSearchViewState(
        val videos: ObservableList<VideoItemView> = ObservableSortedList(VideoItemView::class.java, VideoItemView.sortedListCallback),
        val videosLoadingInProgress: ObservableField<Boolean> = ObservableField(false)
)