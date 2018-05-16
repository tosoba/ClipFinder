package com.example.there.findclips.fragments.dashboard

import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.usecases.spotify.GetAccessToken
import com.example.there.domain.usecases.spotify.GetCategories
import com.example.there.domain.usecases.spotify.GetDailyViralTracks
import com.example.there.domain.usecases.spotify.GetFeaturedPlaylists
import com.example.there.findclips.base.BaseSpotifyViewModel
import com.example.there.findclips.model.entities.TopTrack
import com.example.there.findclips.model.mappers.CategoryEntityMapper
import com.example.there.findclips.model.mappers.PlaylistEntityMapper
import com.example.there.findclips.model.mappers.TrackEntityMapper

class DashboardViewModel(getAccessToken: GetAccessToken,
                         private val getFeaturedPlaylists: GetFeaturedPlaylists,
                         private val getCategories: GetCategories,
                         private val getDailyViralTracks: GetDailyViralTracks) : BaseSpotifyViewModel(getAccessToken) {

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
        addDisposable(getCategories.execute(accessToken)
                .doFinally { viewState.categoriesLoadingInProgress.set(false) }
                .subscribe({ viewState.addCategoriesSorted(it.map(CategoryEntityMapper::mapFrom)) }, this::onError))
    }

    private fun loadFeaturedPlaylists(accessToken: AccessTokenEntity) {
        viewState.featuredPlaylistsLoadingInProgress.set(true)
        addDisposable(getFeaturedPlaylists.execute(accessToken)
                .doFinally { viewState.featuredPlaylistsLoadingInProgress.set(false) }
                .subscribe({ viewState.addFeaturedPlaylistsSorted(it.map(PlaylistEntityMapper::mapFrom)) }, this::onError))
    }

    private fun loadDailyViralTracks(accessToken: AccessTokenEntity) {
        viewState.topTracksLoadingInProgress.set(true)
        addDisposable(getDailyViralTracks.execute(accessToken)
                .doFinally { viewState.topTracksLoadingInProgress.set(false) }
                .subscribe({ viewState.topTracks.addAll(it.map { TopTrack(it.position, TrackEntityMapper.mapFrom(it.track)) }) }, this::onError))
    }

    override fun onError(t: Throwable) {
        super.onError(t)
        handleErrors(t, onErrorsResolved = this::loadDashboardData)
    }
}