package com.example.there.findclips.di.videos

import com.example.there.domain.repos.videos.VideosRepository
import com.example.there.domain.usecase.spotify.AccessTokenUseCase
import com.example.there.domain.usecase.videos.SearchVideosUseCase
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
    fun videosViewModelFactory(accessTokenUseCase: AccessTokenUseCase,
                               searchVideosUseCase: SearchVideosUseCase): VideosViewModelFactory = VideosViewModelFactory(accessTokenUseCase, searchVideosUseCase)
}