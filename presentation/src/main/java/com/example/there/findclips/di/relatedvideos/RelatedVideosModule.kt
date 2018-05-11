package com.example.there.findclips.di.relatedvideos

import com.example.there.domain.repos.videos.VideosRepository
import com.example.there.domain.usecases.videos.GetChannelsThumbnailUrlsUseCase
import com.example.there.domain.usecases.videos.SearchRelatedVideosUseCase
import com.example.there.findclips.relatedvideos.RelatedVideosVMFactory
import com.example.there.findclips.util.rx.AsyncTransformer
import dagger.Module
import dagger.Provides

@Module
class RelatedVideosModule {
    @Provides
    fun searchRelatedVideosUseCase(repository: VideosRepository): SearchRelatedVideosUseCase = SearchRelatedVideosUseCase(AsyncTransformer(), repository)

    @Provides
    fun relatedVideosVMFactory(searchRelatedVideosUseCase: SearchRelatedVideosUseCase,
                               getChannelsThumbnailUrlsUseCase: GetChannelsThumbnailUrlsUseCase): RelatedVideosVMFactory =
            RelatedVideosVMFactory(searchRelatedVideosUseCase, getChannelsThumbnailUrlsUseCase)
}