package com.example.there.findclips.dashboard

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecase.spotify.AccessTokenUseCase
import com.example.there.domain.usecase.spotify.CategoriesUseCase
import com.example.there.domain.usecase.spotify.DailyViralTracksUseCase
import com.example.there.domain.usecase.spotify.FeaturedPlaylistsUseCase

class DashboardViewModelFactory(private val accessTokenUseCase: AccessTokenUseCase,
                                private val featuredPlaylistsUseCase: FeaturedPlaylistsUseCase,
                                private val categoriesUseCase: CategoriesUseCase,
                                private val dailyViralTracksUseCase: DailyViralTracksUseCase) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DashboardViewModel(accessTokenUseCase, featuredPlaylistsUseCase, categoriesUseCase, dailyViralTracksUseCase) as T
    }
}