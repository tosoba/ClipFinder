package com.example.coreandroid.base.fragment

import com.example.coreandroid.model.videos.Video

interface IYoutubeSearchFragment: ISearchFragment {
    val videosLoaded: Boolean
    val videos: List<Video>
}
