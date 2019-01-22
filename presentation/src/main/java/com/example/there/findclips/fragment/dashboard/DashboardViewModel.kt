package com.example.there.findclips.fragment.dashboard

import com.example.there.data.api.spotify.SpotifyApi
import com.example.there.domain.usecase.spotify.GetCategories
import com.example.there.domain.usecase.spotify.GetDailyViralTracks
import com.example.there.domain.usecase.spotify.GetFeaturedPlaylists
import com.example.there.domain.usecase.spotify.GetNewReleases
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.model.entity.TopTrack
import com.example.there.findclips.model.mapper.AlbumEntityMapper
import com.example.there.findclips.model.mapper.CategoryEntityMapper
import com.example.there.findclips.model.mapper.PlaylistEntityMapper
import com.example.there.findclips.model.mapper.TrackEntityMapper
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
        private val getFeaturedPlaylists: GetFeaturedPlaylists,
        private val getCategories: GetCategories,
        private val getDailyViralTracks: GetDailyViralTracks,
        private val getNewReleases: GetNewReleases
) : BaseViewModel() {

    val viewState: DashboardViewState = DashboardViewState()

    fun loadData(onNewReleasesFinally: (() -> Unit)? = null) {
        loadCategories()
        loadFeaturedPlaylists()
        loadDailyViralTracks()
        loadNewReleases(onFinally = onNewReleasesFinally)
    }

    fun loadCategories(shouldClear: Boolean = false) {
        viewState.categoriesLoadingInProgress.set(true)
        addDisposable(getCategories.execute()
                .doFinally { viewState.categoriesLoadingInProgress.set(false) }
                .subscribe({
                    if (shouldClear) viewState.categories.clear()
                    viewState.categories.addAll(it.map(CategoryEntityMapper::mapFrom))
                }, ::onError))
    }

    fun loadFeaturedPlaylists(shouldClear: Boolean = false) {
        viewState.featuredPlaylistsLoadingInProgress.set(true)
        addDisposable(getFeaturedPlaylists.execute()
                .doFinally { viewState.featuredPlaylistsLoadingInProgress.set(false) }
                .subscribe({
                    if (shouldClear) viewState.featuredPlaylists.clear()
                    viewState.featuredPlaylists.addAll(it.map(PlaylistEntityMapper::mapFrom))
                }, ::onError))
    }

    fun loadDailyViralTracks() {
        viewState.topTracksLoadingInProgress.set(true)
        addDisposable(getDailyViralTracks.execute()
                .doFinally { viewState.topTracksLoadingInProgress.set(false) }
                .subscribe({ result ->
                    viewState.topTracks.addAll(result.map { TopTrack(it.position, TrackEntityMapper.mapFrom(it.track)) })
                }, ::onError))
    }

    private var currentNewReleasesOffset: Int = 0
    private var totalNewReleases = 0

    fun loadNewReleases(loadMore: Boolean = false, onFinally: (() -> Unit)? = null) {
        if (viewState.newReleasesLoadingInProgress.get() != true
                && (currentNewReleasesOffset == 0 || (currentNewReleasesOffset < totalNewReleases))) {
            if (!loadMore)
                viewState.newReleasesLoadingInProgress.set(true)
            addDisposable(getNewReleases.execute(currentNewReleasesOffset)
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
}