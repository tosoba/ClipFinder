package com.example.youtuberelatedvideos

import com.airbnb.mvrx.MvRxState
import com.clipfinder.core.model.Empty
import com.clipfinder.core.model.Loadable
import com.clipfinder.core.model.PageTokenList
import com.example.core.android.model.videos.Video

data class YoutubeRelatedState(val videoId: String?, val videos: Loadable<PageTokenList<Video>>) : MvRxState {
    constructor() : this(null, Empty)
}
