package com.example.there.findclips.base

import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.usecases.videos.GetChannelsThumbnailUrlsUseCase

open class BaseVideosViewModel(private val getChannelsThumbnailUrlsUseCase: GetChannelsThumbnailUrlsUseCase) : BaseViewModel() {
    protected fun getChannelThumbnails(videos: List<VideoEntity>, onSuccess: (List<String>) -> Unit) {
        addDisposable(getChannelsThumbnailUrlsUseCase.execute(videos)
                .subscribe(onSuccess, this::onError))
    }
}