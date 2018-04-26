package com.example.there.findclips.di.dashboard

import com.example.there.domain.SpotifyRepository
import com.example.there.domain.entities.CategoryEntity
import com.example.there.domain.usecase.AccessTokenUseCase
import com.example.there.domain.usecase.CategoriesUseCase
import com.example.there.domain.usecase.DailyViralTracksUseCase
import com.example.there.domain.usecase.FeaturedPlaylistsUseCase
import com.example.there.findclips.dashboard.DashboardViewModelFactory
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
                                  dailyViralTracksUseCase: DailyViralTracksUseCase): DashboardViewModelFactory =
            DashboardViewModelFactory(accessTokenUseCase, featuredPlaylistsUseCase, categoriesUseCase, dailyViralTracksUseCase)
}