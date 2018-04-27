package com.example.there.findclips.di.videos

import com.example.there.data.mapper.videos.ChannelThumbnailUrlMapper
import com.example.there.domain.repos.videos.VideosRepository
import com.example.there.domain.usecase.spotify.AccessTokenUseCase
import com.example.there.domain.usecase.videos.GetChannelsThumbnailUrlsUseCase
import com.example.there.domain.usecase.videos.SearchVideosUseCase
import com.example.there.findclips.mappers.VideoEntityMapper
import com.example.there.findclips.util.AsyncTransformer
import com.example.there.findclips.videos.VideosViewModelFactory
import dagger.Module
import dagger.Provides

@VideosScope
@Module
class VideosModule {

    @Provides
    fun searchVideosUseCase(repository: VideosRepository): SearchVideosUseCase = SearchVideosUseCase(AsyncTransformer(), repository)

    @Provides
    fun getChannelsThumbnailUrlsUseCase(repository: VideosRepository): GetChannelsThumbnailUrlsUseCase =
            GetChannelsThumbnailUrlsUseCase(AsyncTransformer(), repository)

    @Provides
    fun videosViewModelFactory(accessTokenUseCase: AccessTokenUseCase,
                               searchVideosUseCase: SearchVideosUseCase,
                               getChannelsThumbnailUrlsUseCase: GetChannelsThumbnailUrlsUseCase,
                               videoEntityMapper: VideoEntityMapper): VideosViewModelFactory =
            VideosViewModelFactory(accessTokenUseCase, searchVideosUseCase, getChannelsThumbnailUrlsUseCase, videoEntityMapper)
}