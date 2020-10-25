package com.example.youtuberelatedvideos

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import com.example.core.android.model.videos.Video
import com.example.core.android.view.recyclerview.item.RecyclerViewItemView

class RelatedVideosViewState(
    val videos: ObservableList<Video> = ObservableArrayList(),
    val initialVideosLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
    val loadingMoreVideosInProgress: ObservableField<Boolean> = ObservableField(false),
    val videosLoadingErrorOccurred: ObservableField<Boolean> = ObservableField(false)
)

class RelatedVideosView(
    val state: RelatedVideosViewState,
    val relatedVideosRecyclerViewItemView: RecyclerViewItemView<Video>
)
