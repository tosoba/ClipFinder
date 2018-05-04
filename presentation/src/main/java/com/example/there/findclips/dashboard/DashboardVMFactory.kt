package com.example.there.findclips.dashboard

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecases.spotify.AccessTokenUseCase
import com.example.there.domain.usecases.spotify.CategoriesUseCase
import com.example.there.domain.usecases.spotify.DailyViralTracksUseCase
import com.example.there.domain.usecases.spotify.FeaturedPlaylistsUseCase

class DashboardVMFactory(private val accessTokenUseCase: AccessTokenUseCase,
                         private val featuredPlaylistsUseCase: FeaturedPlaylistsUseCase,
                         private val categoriesUseCase: CategoriesUseCase,
                         private val dailyViralTracksUseCase: DailyViralTracksUseCase) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DashboardViewModel(accessTokenUseCase, featuredPlaylistsUseCase, categoriesUseCase, dailyViralTracksUseCase) as T
    }
}