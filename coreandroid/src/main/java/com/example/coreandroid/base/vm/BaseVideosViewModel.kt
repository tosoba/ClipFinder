package com.example.coreandroid.base.vm

import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.usecase.videos.GetChannelsThumbnailUrls

open class BaseVideosViewModel(
        private val getChannelsThumbnailUrls: GetChannelsThumbnailUrls
) : BaseViewModel() {

    protected fun getChannelThumbnails(videos: List<VideoEntity>, onSuccess: (List<Pair<Int, String>>) -> Unit) {
        getChannelsThumbnailUrls.execute(videos).subscribeAndDisposeOnCleared(onSuccess, ::onError)
    }
}