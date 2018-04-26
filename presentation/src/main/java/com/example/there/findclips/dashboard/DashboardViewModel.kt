package com.example.there.findclips.dashboard

import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.usecase.spotify.AccessTokenUseCase
import com.example.there.domain.usecase.spotify.CategoriesUseCase
import com.example.there.domain.usecase.spotify.DailyViralTracksUseCase
import com.example.there.domain.usecase.spotify.FeaturedPlaylistsUseCase
import com.example.there.findclips.base.BaseViewModel

class DashboardViewModel(accessTokenUseCase: AccessTokenUseCase,
                         private val featuredPlaylistsUseCase: FeaturedPlaylistsUseCase,
                         private val categoriesUseCase: CategoriesUseCase,
                         private val dailyViralTracksUseCase: DailyViralTracksUseCase) : BaseViewModel(accessTokenUseCase) {

    val viewState: DashboardViewState = DashboardViewState()

    fun loadDashboardData(accessToken: AccessTokenEntity?) {
        if (accessToken != null && accessToken.isValid) {
            accessTokenLiveData.value = accessToken
            loadData(accessTokenLiveData.value!!)
        } else {
            loadAccessToken { loadData(it) }
        }
    }

    private fun loadData(accessToken: AccessTokenEntity) {
        loadCategories(accessToken)
        loadFeaturedPlaylists(accessToken)
        loadDailyViralTracks(accessToken)
    }

    private fun loadCategories(accessToken: AccessTokenEntity) {
        viewState.categoriesLoadingInProgress.set(true)
        addDisposable(categoriesUseCase.getCategories(accessToken)
                .doFinally { viewState.categoriesLoadingInProgress.set(false) }
                .subscribe({ viewState.addCategoriesSorted(it) }, this::onError))
    }

    private fun loadFeaturedPlaylists(accessToken: AccessTokenEntity) {
        viewState.featuredPlaylistsLoadingInProgress.set(true)
        addDisposable(featuredPlaylistsUseCase.getFeaturedPlaylists(accessToken)
                .doFinally { viewState.featuredPlaylistsLoadingInProgress.set(false) }
                .subscribe({ viewState.addFeaturedPlaylistsSorted(it) }, this::onError))
    }

    private fun loadDailyViralTracks(accessToken: AccessTokenEntity) {
        viewState.topTracksLoadingInProgress.set(true)
        addDisposable(dailyViralTracksUseCase.getTracks(accessToken)
                .doFinally { viewState.topTracksLoadingInProgress.set(false) }
                .subscribe({ viewState.topTracks.addAll(it) }, this::onError))
    }

    private fun onError(t: Throwable) {
        errorState.value = t
        handleErrors(t, onErrorsResolved = this::loadDashboardData)
    }
}