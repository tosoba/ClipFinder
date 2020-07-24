package com.example.core.android.base.fragment

import com.example.core.android.model.videos.Video

interface IYoutubeSearchFragment: ISearchFragment {
    val videosLoaded: Boolean
    val videos: List<Video>
}
