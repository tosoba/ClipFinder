package com.clipfinder.youtube.search

import com.airbnb.mvrx.MvRxState
import com.clipfinder.core.android.model.videos.Video
import com.clipfinder.core.model.Empty
import com.clipfinder.core.model.Loadable
import com.clipfinder.core.model.PageTokenList

data class YoutubeSearchState(val query: String, val videos: Loadable<PageTokenList<Video>>) :
    MvRxState {
    constructor(query: String) : this(query, Empty)
}
