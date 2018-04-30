package com.example.there.findclips.di.modules

import com.example.there.data.api.spotify.SpotifyAccountsApi
import com.example.there.data.api.spotify.SpotifyApi
import com.example.there.data.api.spotify.SpotifyChartsApi
import com.example.there.data.api.yahoo.YahooScraper
import com.example.there.data.api.youtube.YoutubeApi
import com.example.there.data.repos.spotify.SpotifyRepositoryImpl
import com.example.there.data.repos.videos.VideosRepositoryImpl
import com.example.there.domain.repos.spotify.SpotifyRepository
import com.example.there.domain.repos.videos.VideosRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun spotifyRepository(api: SpotifyApi,
                          accountsApi: SpotifyAccountsApi,
                          chartsApi: SpotifyChartsApi): SpotifyRepository =
            SpotifyRepositoryImpl(api, accountsApi, chartsApi)

    @Provides
    @Singleton
    fun videosRepository(api: YoutubeApi,
                         scraper: YahooScraper): VideosRepository =
            VideosRepositoryImpl(api, scraper)
}