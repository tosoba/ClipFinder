package com.example.there.findclips.relatedvideos

import android.databinding.ObservableArrayList
import android.support.v7.widget.RecyclerView
import com.example.there.findclips.entities.Video
import com.example.there.findclips.lists.RelatedVideosList

data class RelatedVideosView(
        val state: RelatedVideosViewState,
        val onScrollListener: RecyclerView.OnScrollListener,
        val videosAdapter: RelatedVideosList.Adapter,
        val videosItemDecoration: RecyclerView.ItemDecoration
)

data class RelatedVideosViewState(
        val videos: ObservableArrayList<Video> = ObservableArrayList()
)