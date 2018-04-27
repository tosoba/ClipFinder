package com.example.there.findclips.di.modules

import com.example.there.data.repos.spotify.SpotifyRepositoryImpl
import com.example.there.data.api.spotify.SpotifyAccountsApi
import com.example.there.data.api.spotify.SpotifyApi
import com.example.there.data.api.spotify.SpotifyChartsApi
import com.example.there.data.api.yahoo.YahooScraper
import com.example.there.data.api.youtube.YoutubeApi
import com.example.there.data.entities.videos.VideoData
import com.example.there.data.mapper.spotify.*
import com.example.there.data.mapper.videos.ChannelThumbnailUrlMapper
import com.example.there.data.mapper.videos.VideoMapper
import com.example.there.data.repos.videos.VideosRepositoryImpl
import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.repos.spotify.SpotifyRepository
import com.example.there.domain.repos.videos.VideosRepository
import com.example.there.domain.usecase.spotify.AccessTokenUseCase
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
    fun videosRepository(api: YoutubeApi,
                         scraper: YahooScraper,
                         videoMapper: VideoMapper,
                         channelThumbnailUrlMapper: ChannelThumbnailUrlMapper): VideosRepository =
            VideosRepositoryImpl(api, scraper, videoMapper, channelThumbnailUrlMapper)

    @Provides
    @Singleton
    fun accessTokenUseCase(repository: SpotifyRepository): AccessTokenUseCase = AccessTokenUseCase(AsyncTransformer(), repository)
}