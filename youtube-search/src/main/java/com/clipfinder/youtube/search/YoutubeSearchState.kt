package com.clipfinder.youtube.search

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.PageTokenDataList
import com.example.core.android.model.videos.Video

data class YoutubeSearchState(
    val query: String,
    val videos: PageTokenDataList<Video> = PageTokenDataList()
) : MvRxState {
    constructor(query: String) : this(query, PageTokenDataList())
}
