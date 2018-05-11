package com.example.there.findclips.dashboard

import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.usecases.spotify.AccessTokenUseCase
import com.example.there.domain.usecases.spotify.CategoriesUseCase
import com.example.there.domain.usecases.spotify.DailyViralTracksUseCase
import com.example.there.domain.usecases.spotify.FeaturedPlaylistsUseCase
import com.example.there.findclips.base.BaseSpotifyViewModel
import com.example.there.findclips.entities.TopTrack
import com.example.there.findclips.mappers.CategoryEntityMapper
import com.example.there.findclips.mappers.PlaylistEntityMapper
import com.example.there.findclips.mappers.TrackEntityMapper

class DashboardViewModel(accessTokenUseCase: AccessTokenUseCase,
                         private val featuredPlaylistsUseCase: FeaturedPlaylistsUseCase,
                         private val categoriesUseCase: CategoriesUseCase,
                         private val dailyViralTracksUseCase: DailyViralTracksUseCase) : BaseSpotifyViewModel(accessTokenUseCase) {

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
        addDisposable(categoriesUseCase.execute(accessToken)
                .doFinally { viewState.categoriesLoadingInProgress.set(false) }
                .subscribe({ viewState.addCategoriesSorted(it.map(CategoryEntityMapper::mapFrom)) }, this::onError))
    }

    private fun loadFeaturedPlaylists(accessToken: AccessTokenEntity) {
        viewState.featuredPlaylistsLoadingInProgress.set(true)
        addDisposable(featuredPlaylistsUseCase.execute(accessToken)
                .doFinally { viewState.featuredPlaylistsLoadingInProgress.set(false) }
                .subscribe({ viewState.addFeaturedPlaylistsSorted(it.map(PlaylistEntityMapper::mapFrom)) }, this::onError))
    }

    private fun loadDailyViralTracks(accessToken: AccessTokenEntity) {
        viewState.topTracksLoadingInProgress.set(true)
        addDisposable(dailyViralTracksUseCase.execute(accessToken)
                .doFinally { viewState.topTracksLoadingInProgress.set(false) }
                .subscribe({ viewState.topTracks.addAll(it.map { TopTrack(it.position, TrackEntityMapper.mapFrom(it.track)) }) }, this::onError))
    }

    override fun onError(t: Throwable) {
        super.onError(t)
        handleErrors(t, onErrorsResolved = this::loadDashboardData)
    }
}