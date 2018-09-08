package com.example.there.findclips.fragment.dashboard

import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.usecase.spotify.GetAccessToken
import com.example.there.domain.usecase.spotify.GetCategories
import com.example.there.domain.usecase.spotify.GetDailyViralTracks
import com.example.there.domain.usecase.spotify.GetFeaturedPlaylists
import com.example.there.findclips.base.vm.BaseSpotifyViewModel
import com.example.there.findclips.model.entity.TopTrack
import com.example.there.findclips.model.mapper.CategoryEntityMapper
import com.example.there.findclips.model.mapper.PlaylistEntityMapper
import com.example.there.findclips.model.mapper.TrackEntityMapper
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
        getAccessToken: GetAccessToken,
        private val getFeaturedPlaylists: GetFeaturedPlaylists,
        private val getCategories: GetCategories,
        private val getDailyViralTracks: GetDailyViralTracks
) : BaseSpotifyViewModel(getAccessToken) {

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
                .subscribe({ viewState.categories.addAll(it.map(CategoryEntityMapper::mapFrom)) }, ::onError))
    }

    private fun loadFeaturedPlaylists(accessToken: AccessTokenEntity) {
        viewState.featuredPlaylistsLoadingInProgress.set(true)
        addDisposable(getFeaturedPlaylists.execute(accessToken)
                .doFinally { viewState.featuredPlaylistsLoadingInProgress.set(false) }
                .subscribe({ viewState.featuredPlaylists.addAll(it.map(PlaylistEntityMapper::mapFrom)) }, ::onError))
    }

    private fun loadDailyViralTracks(accessToken: AccessTokenEntity) {
        viewState.topTracksLoadingInProgress.set(true)
        addDisposable(getDailyViralTracks.execute(accessToken)
                .doFinally { viewState.topTracksLoadingInProgress.set(false) }
                .subscribe({ viewState.topTracks.addAll(it.map { TopTrack(it.position, TrackEntityMapper.mapFrom(it.track)) }) }, ::onError))
    }

    override fun onError(t: Throwable) {
        super.onError(t)
        handleErrors(t, onErrorsResolved = this::loadDashboardData)
    }
}