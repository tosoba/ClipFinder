package com.example.there.findclips.di.modules

import com.example.there.domain.repos.spotify.SpotifyRepository
import com.example.there.domain.repos.videos.VideosRepository
import com.example.there.domain.usecases.spotify.AccessTokenUseCase
import com.example.there.domain.usecases.videos.GetChannelsThumbnailUrlsUseCase
import com.example.there.findclips.util.rx.AsyncTransformer
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
    fun getChannelsThumbnailUrlsUseCase(repository: VideosRepository): GetChannelsThumbnailUrlsUseCase =
            GetChannelsThumbnailUrlsUseCase(AsyncTransformer(), repository)
}