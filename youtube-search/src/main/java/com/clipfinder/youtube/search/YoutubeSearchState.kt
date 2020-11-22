package com.clipfinder.youtube.search

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.Empty
import com.example.core.android.model.Loadable
import com.example.core.android.model.PageTokenList
import com.example.core.android.model.videos.Video

data class YoutubeSearchState(val query: String, val videos: Loadable<PageTokenList<Video>>) : MvRxState {
    constructor(query: String) : this(query, Empty)
}
