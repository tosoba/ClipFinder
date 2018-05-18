package com.example.there.findclips.di.relatedvideos

import com.example.there.domain.repos.videos.IVideosRepository
import com.example.there.domain.usecases.videos.GetChannelsThumbnailUrls
import com.example.there.domain.usecases.videos.SearchRelatedVideos
import com.example.there.findclips.fragments.relatedvideos.RelatedVideosVMFactory
import com.example.there.findclips.util.rx.AsyncTransformer
import dagger.Module
import dagger.Provides

@Module
class RelatedVideosModule {
    @Provides
    fun searchRelatedVideosUseCase(repository: IVideosRepository): SearchRelatedVideos = SearchRelatedVideos(AsyncTransformer(), repository)

    @Provides
    fun relatedVideosVMFactory(searchRelatedVideos: SearchRelatedVideos,
                               getChannelsThumbnailUrls: GetChannelsThumbnailUrls): RelatedVideosVMFactory =
            RelatedVideosVMFactory(searchRelatedVideos, getChannelsThumbnailUrls)
}