package com.example.there.findclips.search.videos

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecases.videos.GetChannelsThumbnailUrlsUseCase
import com.example.there.domain.usecases.videos.SearchVideosUseCase

class VideosSearchVMFactory(private val searchVideosUseCase: SearchVideosUseCase,
                            private val getChannelsThumbnailUrlsUseCase: GetChannelsThumbnailUrlsUseCase) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VideosSearchViewModel(searchVideosUseCase, getChannelsThumbnailUrlsUseCase) as T
    }
}