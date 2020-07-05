package com.example.youtubesearch

import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import com.example.coreandroid.util.list.ObservableSortedList
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemView
import com.example.coreandroid.view.recyclerview.item.VideoItemView

class VideosSearchView(
    val state: VideosSearchViewState,
    val recyclerViewItemView: RecyclerViewItemView<VideoItemView>
)

class VideosSearchViewState(
    val videos: ObservableList<VideoItemView> = ObservableSortedList(VideoItemView::class.java, VideoItemView.sortedListCallback),
    val videosLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
    val videosLoadingErrorOccurred: ObservableField<Boolean> = ObservableField(false)
)
