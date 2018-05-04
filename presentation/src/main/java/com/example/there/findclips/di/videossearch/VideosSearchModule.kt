package com.example.there.findclips.di.videossearch

import com.example.there.domain.repos.videos.VideosRepository
import com.example.there.domain.usecases.videos.GetChannelsThumbnailUrlsUseCase
import com.example.there.domain.usecases.videos.SearchVideosUseCase
import com.example.there.findclips.search.videos.VideosSearchVMFactory
import com.example.there.findclips.util.AsyncTransformer
import dagger.Module
import dagger.Provides


@VideosSearchScope
@Module
class VideosSearchModule {

    @Provides
    fun videosViewModelFactory(searchVideosUseCase: SearchVideosUseCase,
                               getChannelsThumbnailUrlsUseCase: GetChannelsThumbnailUrlsUseCase): VideosSearchVMFactory =
            VideosSearchVMFactory(searchVideosUseCase, getChannelsThumbnailUrlsUseCase)

    @Provides
    fun searchVideosUseCase(repository: VideosRepository): SearchVideosUseCase = SearchVideosUseCase(AsyncTransformer(), repository)

    @Provides
    fun getChannelsThumbnailUrlsUseCase(repository: VideosRepository): GetChannelsThumbnailUrlsUseCase =
            GetChannelsThumbnailUrlsUseCase(AsyncTransformer(), repository)
}