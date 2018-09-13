package com.example.there.findclips.fragment.search.videos

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.example.there.findclips.model.entity.Video
import com.example.there.findclips.view.list.item.RecyclerViewItemView

class VideosSearchView(
        val state: VideosSearchViewState,
        val recyclerViewItemView: RecyclerViewItemView<Video>
)

data class VideosSearchViewState(
        val videos: ObservableArrayList<Video> = ObservableArrayList(),
        val videosLoadingInProgress: ObservableField<Boolean> = ObservableField(false)
)