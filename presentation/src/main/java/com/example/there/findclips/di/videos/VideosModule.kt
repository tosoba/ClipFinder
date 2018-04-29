package com.example.there.findclips.di.videos

import com.example.there.domain.usecase.videos.GetChannelsThumbnailUrlsUseCase
import com.example.there.domain.usecase.videos.SearchVideosUseCase
import com.example.there.findclips.mappers.VideoEntityMapper
import com.example.there.findclips.videossearch.VideosSearchVMFactory
import dagger.Module
import dagger.Provides

@VideosScope
@Module
class VideosModule {

    @Provides
    fun videosViewModelFactory(searchVideosUseCase: SearchVideosUseCase,
                               getChannelsThumbnailUrlsUseCase: GetChannelsThumbnailUrlsUseCase,
                               videoEntityMapper: VideoEntityMapper): VideosSearchVMFactory =
            VideosSearchVMFactory(searchVideosUseCase, getChannelsThumbnailUrlsUseCase, videoEntityMapper)
}