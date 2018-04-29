package com.example.there.findclips.di.dashboard

import com.example.there.domain.repos.spotify.SpotifyRepository
import com.example.there.domain.usecase.spotify.AccessTokenUseCase
import com.example.there.domain.usecase.spotify.CategoriesUseCase
import com.example.there.domain.usecase.spotify.DailyViralTracksUseCase
import com.example.there.domain.usecase.spotify.FeaturedPlaylistsUseCase
import com.example.there.findclips.dashboard.DashboardVMFactory
import com.example.there.findclips.util.AsyncTransformer
import dagger.Module
import dagger.Provides

@DashboardScope
@Module
class DashboardModule {

    @Provides
    fun categoriesUseCase(repository: SpotifyRepository): CategoriesUseCase = CategoriesUseCase(AsyncTransformer(), repository)

    @Provides
    fun featuredPlaylistsUseCase(repository: SpotifyRepository): FeaturedPlaylistsUseCase = FeaturedPlaylistsUseCase(AsyncTransformer(), repository)

    @Provides
    fun dailyViralTracksUseCase(repository: SpotifyRepository): DailyViralTracksUseCase = DailyViralTracksUseCase(AsyncTransformer(), repository)

    @Provides
    fun dashboardViewModelFactory(accessTokenUseCase: AccessTokenUseCase,
                                  featuredPlaylistsUseCase: FeaturedPlaylistsUseCase,
                                  categoriesUseCase: CategoriesUseCase,
                                  dailyViralTracksUseCase: DailyViralTracksUseCase): DashboardVMFactory =
            DashboardVMFactory(accessTokenUseCase, featuredPlaylistsUseCase, categoriesUseCase, dailyViralTracksUseCase)
}