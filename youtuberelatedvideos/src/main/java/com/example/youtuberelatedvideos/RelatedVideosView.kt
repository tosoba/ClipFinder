package com.example.youtuberelatedvideos

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
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