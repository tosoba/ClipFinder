package com.example.there.findclips.relatedvideos

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecases.videos.GetChannelsThumbnailUrlsUseCase
import com.example.there.domain.usecases.videos.SearchRelatedVideosUseCase

class RelatedVideosVMFactory(private val searchRelatedVideosUseCase: SearchRelatedVideosUseCase,
                             private val getChannelsThumbnailUrlsUseCase: GetChannelsThumbnailUrlsUseCase) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            RelatedVideosViewModel(searchRelatedVideosUseCase, getChannelsThumbnailUrlsUseCase) as T
}