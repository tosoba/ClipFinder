package com.example.there.findclips.di.spotifysearch

import com.example.there.domain.repos.spotify.SpotifyRepository
import com.example.there.domain.usecases.spotify.AccessTokenUseCase
import com.example.there.domain.usecases.spotify.SearchAllUseCase
import com.example.there.findclips.search.spotify.SpotifySearchVMFactory
import com.example.there.findclips.util.AsyncTransformer
import dagger.Module
import dagger.Provides

@SpotifySearchScope
@Module
class SpotifySearchModule {

    @Provides
    fun searchAllUseCase(repository: SpotifyRepository): SearchAllUseCase = SearchAllUseCase(AsyncTransformer(), repository)

    @Provides
    fun searchViewModelFactory(accessTokenUseCase: AccessTokenUseCase,
                               searchAllUseCase: SearchAllUseCase): SpotifySearchVMFactory =
            SpotifySearchVMFactory(accessTokenUseCase, searchAllUseCase)
}