package com.example.there.findclips.di.search

import com.example.there.domain.usecase.spotify.AccessTokenUseCase
import com.example.there.findclips.spotifysearch.SpotifySearchVMFactory
import dagger.Module
import dagger.Provides

@SearchScope
@Module
class SearchModule {

    @Provides
    fun searchViewModelFactory(accessTokenUseCase: AccessTokenUseCase): SpotifySearchVMFactory = SpotifySearchVMFactory(accessTokenUseCase)
}