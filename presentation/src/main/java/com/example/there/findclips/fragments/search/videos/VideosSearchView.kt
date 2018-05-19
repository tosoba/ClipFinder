package com.example.there.findclips.fragments.search.videos

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.support.v7.widget.RecyclerView
import com.example.there.findclips.model.entities.Video
import com.example.there.findclips.view.lists.VideosList

data class VideosSearchView(
        val state: VideosSearchViewState,
        val onScrollListener: RecyclerView.OnScrollListener,
        val videosAdapter: VideosList.Adapter,
        val videosItemDecoration: RecyclerView.ItemDecoration
)

data class VideosSearchViewState(
        val videos: ObservableArrayList<Video> = ObservableArrayList(),
        val videosLoadingInProgress: ObservableField<Boolean> = ObservableField(false)
)