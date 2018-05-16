package com.example.there.findclips.base

import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.usecases.videos.GetChannelsThumbnailUrls

open class BaseVideosViewModel(private val getChannelsThumbnailUrls: GetChannelsThumbnailUrls) : BaseViewModel() {
    protected fun getChannelThumbnails(videos: List<VideoEntity>, onSuccess: (List<String>) -> Unit) {
        addDisposable(getChannelsThumbnailUrls.execute(videos)
                .subscribe(onSuccess, this::onError))
    }
}