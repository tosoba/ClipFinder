package com.example.there.findclips.spotify.dashboard

import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.CategoryEntity
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.usecase.spotify.GetCategories
import com.example.there.domain.usecase.spotify.GetDailyViralTracks
import com.example.there.domain.usecase.spotify.GetFeaturedPlaylists
import com.example.there.domain.usecase.spotify.GetNewReleases
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.model.entity.spotify.TopTrack
import com.example.there.findclips.model.mapper.spotify.ui
import javax.inject.Inject

class SpotifyDashboardViewModel @Inject constructor(
        private val getFeaturedPlaylists: GetFeaturedPlaylists,
        private val getCategories: GetCategories,
        private val getDailyViralTracks: GetDailyViralTracks,
        private val getNewReleases: GetNewReleases
) : BaseViewModel() {

    val viewState: SpotifyDashboardViewState = SpotifyDashboardViewState()

    fun loadData() {
        loadCategories()
        loadFeaturedPlaylists()
        loadDailyViralTracks()
        loadNewReleases()
    }

    fun loadCategories(shouldClear: Boolean = false) {
        viewState.categoriesLoadingInProgress.set(true)
        getCategories.execute()
                .doFinally { viewState.categoriesLoadingInProgress.set(false) }
                .subscribeAndDisposeOnCleared({
                    if (shouldClear) viewState.categories.clear()
                    viewState.categories.addAll(it.map(CategoryEntity::ui))
                    viewState.categoriesLoadingErrorOccurred.set(false)
                }, getOnErrorWith {
                    viewState.categoriesLoadingErrorOccurred.set(true)
                })
    }

    fun loadFeaturedPlaylists(shouldClear: Boolean = false) {
        viewState.featuredPlaylistsLoadingInProgress.set(true)
        getFeaturedPlaylists.execute()
                .doFinally { viewState.featuredPlaylistsLoadingInProgress.set(false) }
                .subscribeAndDisposeOnCleared({
                    if (shouldClear) viewState.featuredPlaylists.clear()
                    viewState.featuredPlaylists.addAll(it.map(PlaylistEntity::ui))
                    viewState.featuredPlaylistsLoadingErrorOccurred.set(false)
                }, getOnErrorWith {
                    viewState.featuredPlaylistsLoadingErrorOccurred.set(true)
                })
    }

    fun loadDailyViralTracks() {
        viewState.topTracksLoadingInProgress.set(true)
        getDailyViralTracks.execute()
                .doFinally { viewState.topTracksLoadingInProgress.set(false) }
                .subscribeAndDisposeOnCleared({ result ->
                    viewState.topTracks.addAll(result.map { TopTrack(it.position, it.track.ui) })
                    viewState.topTracksLoadingErrorOccurred.set(false)
                }, getOnErrorWith {
                    viewState.topTracksLoadingErrorOccurred.set(true)
                })
    }

    private var currentNewReleasesOffset: Int = 0
    private var totalNewReleases = 0

    fun loadNewReleases(loadMore: Boolean = false) {
        if (viewState.newReleasesLoadingInProgress.get() != true
                && (currentNewReleasesOffset == 0 || (currentNewReleasesOffset < totalNewReleases))) {
            if (!loadMore) viewState.newReleasesLoadingInProgress.set(true)
            getNewReleases.execute(currentNewReleasesOffset)
                    .doFinally {
                        if (!loadMore) viewState.newReleasesLoadingInProgress.set(false)
                    }
                    .subscribeAndDisposeOnCleared({
                        currentNewReleasesOffset = it.offset + SpotifyApi.DEFAULT_LIMIT
                        totalNewReleases = it.totalItems
                        viewState.newReleases.addAll(it.items.map(AlbumEntity::ui))
                        viewState.newReleasesLoadingErrorOccurred.set(false)
                    }, getOnErrorWith {
                        viewState.newReleasesLoadingErrorOccurred.set(true)
                    })
        }
    }
}