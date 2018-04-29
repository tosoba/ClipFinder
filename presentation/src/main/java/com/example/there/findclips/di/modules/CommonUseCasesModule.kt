package com.example.there.findclips.di.modules

import com.example.there.domain.repos.spotify.SpotifyRepository
import com.example.there.domain.repos.videos.VideosRepository
import com.example.there.domain.usecase.spotify.AccessTokenUseCase
import com.example.there.domain.usecase.videos.GetChannelsThumbnailUrlsUseCase
import com.example.there.domain.usecase.videos.SearchVideosUseCase
import com.example.there.findclips.util.AsyncTransformer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CommonUseCasesModule {

    @Provides
    @Singleton
    fun accessTokenUseCase(repository: SpotifyRepository): AccessTokenUseCase = AccessTokenUseCase(AsyncTransformer(), repository)

    @Provides
    @Singleton
    fun searchVideosUseCase(repository: VideosRepository): SearchVideosUseCase = SearchVideosUseCase(AsyncTransformer(), repository)

    @Provides
    @Singleton
    fun getChannelsThumbnailUrlsUseCase(repository: VideosRepository): GetChannelsThumbnailUrlsUseCase =
            GetChannelsThumbnailUrlsUseCase(AsyncTransformer(), repository)
}