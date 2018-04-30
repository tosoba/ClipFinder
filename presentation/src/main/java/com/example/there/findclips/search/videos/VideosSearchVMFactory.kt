package com.example.there.findclips.search.videos

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.usecase.videos.GetChannelsThumbnailUrlsUseCase
import com.example.there.domain.usecase.videos.SearchVideosUseCase
import com.example.there.findclips.entities.Video

class VideosSearchVMFactory(private val searchVideosUseCase: SearchVideosUseCase,
                            private val getChannelsThumbnailUrlsUseCase: GetChannelsThumbnailUrlsUseCase,
                            private val videoEntityMapper: Mapper<VideoEntity, Video>) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VideosSearchViewModel(searchVideosUseCase, getChannelsThumbnailUrlsUseCase, videoEntityMapper) as T
    }
}