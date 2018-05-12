package com.example.there.findclips.di.spotifysearch

import com.example.there.domain.repos.spotify.SpotifyRepository
import com.example.there.domain.usecases.spotify.GetAccessToken
import com.example.there.domain.usecases.spotify.SearchSpotify
import com.example.there.findclips.search.spotify.SpotifySearchVMFactory
import com.example.there.findclips.util.rx.AsyncTransformer
import dagger.Module
import dagger.Provides

@SpotifySearchScope
@Module
class SpotifySearchModule {

    @Provides
    fun searchAllUseCase(repository: SpotifyRepository): SearchSpotify = SearchSpotify(AsyncTransformer(), repository)

    @Provides
    fun searchViewModelFactory(getAccessToken: GetAccessToken,
                               searchSpotify: SearchSpotify): SpotifySearchVMFactory =
            SpotifySearchVMFactory(getAccessToken, searchSpotify)
}