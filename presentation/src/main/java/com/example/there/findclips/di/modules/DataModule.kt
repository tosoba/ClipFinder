package com.example.there.findclips.di.modules

import com.example.there.data.SpotifyRepositoryImpl
import com.example.there.data.api.SpotifyAccountsApi
import com.example.there.data.api.SpotifyApi
import com.example.there.data.api.SpotifyChartsApi
import com.example.there.data.mapper.*
import com.example.there.domain.SpotifyRepository
import com.example.there.domain.usecase.AccessTokenUseCase
import com.example.there.findclips.util.AsyncTransformer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun spotifyRepository(api: SpotifyApi,
                          accountsApi: SpotifyAccountsApi,
                          chartsApi: SpotifyChartsApi,
                          categoryMapper: CategoryMapper,
                          playlistMapper: PlaylistMapper,
                          trackMapper: TrackMapper,
                          chartTrackIdMapper: ChartTrackIdMapper,
                          accessTokenMapper: AccessTokenMapper): SpotifyRepository =
            SpotifyRepositoryImpl(api, accountsApi, chartsApi, categoryMapper, playlistMapper, trackMapper, chartTrackIdMapper, accessTokenMapper)

    @Provides
    @Singleton
    fun accessTokenUseCase(repository: SpotifyRepository): AccessTokenUseCase = AccessTokenUseCase(AsyncTransformer(), repository)
}