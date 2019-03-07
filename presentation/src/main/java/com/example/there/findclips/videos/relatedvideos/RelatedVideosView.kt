package com.example.there.findclips.videos.relatedvideos

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.example.there.findclips.view.list.item.RecyclerViewItemView
import com.example.there.findclips.view.list.item.VideoItemView

class RelatedVideosViewState(
        val videos: ObservableList<VideoItemView> = ObservableArrayList(),
        val initialVideosLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val loadingMoreVideosInProgress: ObservableField<Boolean> = ObservableField(false)
)

class RelatedVideosView(
        val state: RelatedVideosViewState,
        val relatedVideosRecyclerViewItemView: RecyclerViewItemView<VideoItemView>
)