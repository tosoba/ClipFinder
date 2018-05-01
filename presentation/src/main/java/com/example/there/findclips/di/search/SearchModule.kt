package com.example.there.findclips.di.search

import com.example.there.domain.repos.spotify.SpotifyRepository
import com.example.there.domain.usecase.spotify.AccessTokenUseCase
import com.example.there.domain.usecase.spotify.SearchAllUseCase
import com.example.there.domain.usecase.videos.GetChannelsThumbnailUrlsUseCase
import com.example.there.domain.usecase.videos.SearchVideosUseCase
import com.example.there.findclips.search.spotify.SpotifySearchVMFactory
import com.example.there.findclips.search.videos.VideosSearchVMFactory
import com.example.there.findclips.util.AsyncTransformer
import dagger.Module
import dagger.Provides

@SearchScope
@Module
class SearchModule {

    @Provides
    fun searchAllUseCase(repository: SpotifyRepository): SearchAllUseCase = SearchAllUseCase(AsyncTransformer(), repository)

    @Provides
    fun videosViewModelFactory(searchVideosUseCase: SearchVideosUseCase,
                               getChannelsThumbnailUrlsUseCase: GetChannelsThumbnailUrlsUseCase): VideosSearchVMFactory =
            VideosSearchVMFactory(searchVideosUseCase, getChannelsThumbnailUrlsUseCase)

    @Provides
    fun searchViewModelFactory(accessTokenUseCase: AccessTokenUseCase,
                               searchAllUseCase: SearchAllUseCase): SpotifySearchVMFactory =
            SpotifySearchVMFactory(accessTokenUseCase, searchAllUseCase)
}