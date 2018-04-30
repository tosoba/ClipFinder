package com.example.there.findclips.di.search

import com.example.there.domain.usecase.spotify.AccessTokenUseCase
import com.example.there.domain.usecase.videos.GetChannelsThumbnailUrlsUseCase
import com.example.there.domain.usecase.videos.SearchVideosUseCase
import com.example.there.findclips.search.spotify.SpotifySearchVMFactory
import com.example.there.findclips.search.videos.VideosSearchVMFactory
import dagger.Module
import dagger.Provides

@SearchScope
@Module
class SearchModule {

    @Provides
    fun videosViewModelFactory(searchVideosUseCase: SearchVideosUseCase,
                               getChannelsThumbnailUrlsUseCase: GetChannelsThumbnailUrlsUseCase): VideosSearchVMFactory =
            VideosSearchVMFactory(searchVideosUseCase, getChannelsThumbnailUrlsUseCase)

    @Provides
    fun searchViewModelFactory(accessTokenUseCase: AccessTokenUseCase): SpotifySearchVMFactory = SpotifySearchVMFactory(accessTokenUseCase)
}