package com.example.there.findclips.search.videos

import android.support.v7.widget.RecyclerView
import com.example.there.findclips.lists.VideosList

data class VideosSearchView(
        val state: VideosSearchViewState,
        val onScrollListener: RecyclerView.OnScrollListener,
        val videosAdapter: VideosList.Adapter,
        val videosItemDecoration: RecyclerView.ItemDecoration
)