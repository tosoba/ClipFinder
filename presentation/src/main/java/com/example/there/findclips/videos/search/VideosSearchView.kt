package com.example.there.findclips.videos.search

import android.databinding.ObservableField
import android.databinding.ObservableList

class VideosSearchView(
        val state: VideosSearchViewState,
        val recyclerViewItemView: RecyclerViewItemView<VideoItemView>
)

class VideosSearchViewState(
        val videos: ObservableList<VideoItemView> = ObservableSortedList(VideoItemView::class.java, VideoItemView.sortedListCallback),
        val videosLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val videosLoadingErrorOccurred: ObservableField<Boolean> = ObservableField(false)
)