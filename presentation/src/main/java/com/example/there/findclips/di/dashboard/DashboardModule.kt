package com.example.there.findclips.di.dashboard

import com.example.there.domain.repos.spotify.SpotifyRepository
import com.example.there.domain.usecases.spotify.GetAccessToken
import com.example.there.domain.usecases.spotify.GetCategories
import com.example.there.domain.usecases.spotify.GetDailyViralTracks
import com.example.there.domain.usecases.spotify.GetFeaturedPlaylists
import com.example.there.findclips.dashboard.DashboardVMFactory
import com.example.there.findclips.util.rx.AsyncTransformer
import dagger.Module
import dagger.Provides

@DashboardScope
@Module
class DashboardModule {

    @Provides
    fun categoriesUseCase(repository: SpotifyRepository): GetCategories = GetCategories(AsyncTransformer(), repository)

    @Provides
    fun featuredPlaylistsUseCase(repository: SpotifyRepository): GetFeaturedPlaylists = GetFeaturedPlaylists(AsyncTransformer(), repository)

    @Provides
    fun dailyViralTracksUseCase(repository: SpotifyRepository): GetDailyViralTracks = GetDailyViralTracks(AsyncTransformer(), repository)

    @Provides
    fun dashboardViewModelFactory(getAccessToken: GetAccessToken,
                                  getFeaturedPlaylists: GetFeaturedPlaylists,
                                  getCategories: GetCategories,
                                  getDailyViralTracks: GetDailyViralTracks): DashboardVMFactory =
            DashboardVMFactory(getAccessToken, getFeaturedPlaylists, getCategories, getDailyViralTracks)
}