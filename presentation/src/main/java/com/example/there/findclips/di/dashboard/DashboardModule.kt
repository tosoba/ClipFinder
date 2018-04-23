package com.example.there.findclips.di.dashboard

import com.example.there.domain.SpotifyRepository
import com.example.there.domain.entities.CategoryEntity
import com.example.there.domain.usecase.AccessTokenUseCase
import com.example.there.domain.usecase.CategoriesUseCase
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
    fun dashboardViewModelFactory(accessTokenUseCase: AccessTokenUseCase, categoriesUseCase: CategoriesUseCase): DashboardViewModelFactory =
            DashboardViewModelFactory(accessTokenUseCase, categoriesUseCase)
}