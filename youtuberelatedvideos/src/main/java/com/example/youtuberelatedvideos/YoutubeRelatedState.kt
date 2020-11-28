package com.example.youtuberelatedvideos

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.Empty
import com.example.core.android.model.Loadable
import com.example.core.android.model.PageTokenList
import com.example.core.android.model.videos.Video

data class YoutubeRelatedState(val videoId: String?, val videos: Loadable<PageTokenList<Video>>) : MvRxState {
    constructor() : this(null, Empty)
}
