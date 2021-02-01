package com.clipfinder.youtube.search

import com.airbnb.mvrx.MvRxState
import com.clipfinder.core.model.Empty
import com.clipfinder.core.model.Loadable
import com.clipfinder.core.model.PageTokenList
import com.example.core.android.model.videos.Video

data class YoutubeSearchState(val query: String, val videos: Loadable<PageTokenList<Video>>) : MvRxState {
    constructor(query: String) : this(query, Empty)
}
