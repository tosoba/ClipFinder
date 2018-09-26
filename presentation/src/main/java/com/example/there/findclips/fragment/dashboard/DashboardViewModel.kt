package com.example.there.findclips.fragment.dashboard

import com.example.there.data.api.spotify.SpotifyApi
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.usecase.spotify.*
import com.example.there.findclips.base.vm.BaseSpotifyViewModel
import com.example.there.findclips.model.entity.TopTrack
import com.example.there.findclips.model.mapper.AlbumEntityMapper
import com.example.there.findclips.model.mapper.CategoryEntityMapper
import com.example.there.findclips.model.mapper.PlaylistEntityMapper
import com.example.there.findclips.model.mapper.TrackEntityMapper
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
        getAccessToken: GetAccessToken,
        private val getFeaturedPlaylists: GetFeaturedPlaylists,
        private val getCategories: GetCategories,
        private val getDailyViralTracks: GetDailyViralTracks,
        private val getNewReleases: GetNewReleases
) : BaseSpotifyViewModel(getAccessToken) {

    val viewState: DashboardViewState = DashboardViewState()

    fun loadDashboardData(
            accessToken: AccessTokenEntity?,
            onNewReleasesFinally: (() -> Unit)? = null
    ) {
        if (accessToken != null && accessToken.isValid) {
            accessTokenLiveData.value = accessToken
            loadData(accessTokenLiveData.value!!, onNewReleasesFinally)
        } else {
            loadAccessToken { loadData(it) }
        }
    }

    private fun loadData(
            accessToken: AccessTokenEntity,
            onNewReleasesFinally: (() -> Unit)? = null
    ) = with(accessToken) {
        loadCategories(this)
        loadFeaturedPlaylists(this)
        loadDailyViralTracks(this)
        loadNewReleases(this, onFinally = onNewReleasesFinally)
    }

    fun loadCategories(accessToken: AccessTokenEntity, shouldClear: Boolean = false) {
        viewState.categoriesLoadingInProgress.set(true)
        addDisposable(getCategories.execute(accessToken)
                .doFinally { viewState.categoriesLoadingInProgress.set(false) }
                .subscribe({
                    if (shouldClear) viewState.categories.clear()
                    viewState.categories.addAll(it.map(CategoryEntityMapper::mapFrom))
                }, ::onError))
    }

    fun loadFeaturedPlaylists(accessToken: AccessTokenEntity, shouldClear: Boolean = false) {
        viewState.featuredPlaylistsLoadingInProgress.set(true)
        addDisposable(getFeaturedPlaylists.execute(accessToken)
                .doFinally { viewState.featuredPlaylistsLoadingInProgress.set(false) }
                .subscribe({
                    if (shouldClear) viewState.featuredPlaylists.clear()
                    viewState.featuredPlaylists.addAll(it.map(PlaylistEntityMapper::mapFrom))
                }, ::onError))
    }

    fun loadDailyViralTracks(accessToken: AccessTokenEntity) {
        viewState.topTracksLoadingInProgress.set(true)
        addDisposable(getDailyViralTracks.execute(accessToken)
                .doFinally { viewState.topTracksLoadingInProgress.set(false) }
                .subscribe({ result ->
                    viewState.topTracks.addAll(result.map { TopTrack(it.position, TrackEntityMapper.mapFrom(it.track)) })
                }, ::onError))
    }

    private var currentNewReleasesOffset: Int = 0
    private var totalNewReleases = 0

    fun loadNewReleases(accessToken: AccessTokenEntity, loadMore: Boolean = false, onFinally: (() -> Unit)? = null) {
        if (viewState.newReleasesLoadingInProgress.get() != true
                && (currentNewReleasesOffset == 0 || (currentNewReleasesOffset < totalNewReleases))) {
            if (!loadMore)
                viewState.newReleasesLoadingInProgress.set(true)
            addDisposable(getNewReleases.execute(accessToken, currentNewReleasesOffset)
                    .doFinally {
                        if (!loadMore)
                            viewState.newReleasesLoadingInProgress.set(false)
                        onFinally?.invoke()
                    }
                    .subscribe({
                        currentNewReleasesOffset = it.offset + SpotifyApi.DEFAULT_LIMIT
                        totalNewReleases = it.totalItems
                        viewState.newReleases.addAll(it.items.map(AlbumEntityMapper::mapFrom))
                    }, ::onError))
        }
    }

    override fun onError(t: Throwable) {
        super.onError(t)
        handleErrors(t, onErrorsResolved = { loadDashboardData(it) })
    }
}