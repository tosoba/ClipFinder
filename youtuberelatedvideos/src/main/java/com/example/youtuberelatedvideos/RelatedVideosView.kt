package com.example.youtuberelatedvideos

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemView
import com.example.coreandroid.view.recyclerview.item.VideoItemView

class RelatedVideosViewState(
        val videos: ObservableList<VideoItemView> = ObservableArrayList(),
        val initialVideosLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val loadingMoreVideosInProgress: ObservableField<Boolean> = ObservableField(false),
        val videosLoadingErrorOccurred: ObservableField<Boolean> = ObservableField(false)
)

class RelatedVideosView(
        val state: RelatedVideosViewState,
        val relatedVideosRecyclerViewItemView: RecyclerViewItemView<VideoItemView>
)