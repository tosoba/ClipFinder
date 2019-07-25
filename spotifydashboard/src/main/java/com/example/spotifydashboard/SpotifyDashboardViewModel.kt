package com.example.spotifydashboard

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.model.mapData
import com.example.coreandroid.base.vm.MvRxViewModel
import com.example.coreandroid.mapper.spotify.ui
import com.example.coreandroid.model.Loading
import com.example.coreandroid.model.spotify.TopTrack
import com.example.spotifyapi.SpotifyApi
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.CategoryEntity
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.usecase.spotify.GetCategories
import com.example.there.domain.usecase.spotify.GetDailyViralTracks
import com.example.there.domain.usecase.spotify.GetFeaturedPlaylists
import com.example.there.domain.usecase.spotify.GetNewReleases
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class SpotifyDashboardViewModel(
        initialState: SpotifyDashboardViewState,
        private val getCategories: GetCategories,
        private val getFeaturedPlaylists: GetFeaturedPlaylists,
        private val getNewReleases: GetNewReleases,
        private val getDailyViralTracks: GetDailyViralTracks
) : MvRxViewModel<SpotifyDashboardViewState>(initialState) {

    init {
        loadData()
    }

    fun loadData() {
        loadCategories()
        loadFeaturedPlaylists()
        loadDailyViralTracks()
        loadNewReleases()
    }

    //TODO: maybe use Timber for logging in onError
    fun loadCategories() = withState { state ->
        if (state.categories.status is Loading) return@withState

        getCategories(applySchedulers = false)
                .mapData { categories ->
                    categories.map(CategoryEntity::ui).sortedBy { it.name }
                }
                .subscribeOn(Schedulers.io())
                .updateWithResource(state::categories) { copy(categories = it) }
    }


    fun loadFeaturedPlaylists() = withState { state ->
        if (state.featuredPlaylists.status is Loading) return@withState

        getFeaturedPlaylists(applySchedulers = false)
                .mapData { playlists ->
                    playlists.map(PlaylistEntity::ui).sortedBy { it.name }
                }
                .subscribeOn(Schedulers.io())
                .updateWithResource(state::featuredPlaylists) {
                    copy(featuredPlaylists = it)
                }
    }


    fun loadDailyViralTracks() = withState { state ->
        if (state.topTracks.status is Loading) return@withState

        getDailyViralTracks(applySchedulers = false)
                .mapData { tracks ->
                    tracks.map { TopTrack(it.position, it.track.ui) }
                            .sortedBy { it.position }
                }
                .subscribeOn(Schedulers.io())
                .updateWithResource(state::topTracks) { copy(topTracks = it) }
    }


    private var currentNewReleasesOffset: Int = 0
    private var totalNewReleases = 0

    fun loadNewReleases(loadMore: Boolean = false) {
        withState { state ->
            state.newReleases.ifNotLoadingAndNotAllLoaded {
                getNewReleases(applySchedulers = false, args = state.newReleases.offset)
                        .mapData { listPage -> listPage.items.map(AlbumEntity::ui) }
                        .subscribeOn(Schedulers.io())
                        .updateWithResource(state::newReleases) {
                            copy(newReleases = it)
                        }
            }
        }

        if (viewState.newReleasesLoadingInProgress.get() != true
                && (currentNewReleasesOffset == 0 || (currentNewReleasesOffset < totalNewReleases))) {
            if (!loadMore) viewState.newReleasesLoadingInProgress.set(true)
            getNewReleases(currentNewReleasesOffset)
                    .takeSuccessOnly()
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

    companion object : MvRxViewModelFactory<SpotifyDashboardViewModel, SpotifyDashboardViewState> {
        override fun create(
                viewModelContext: ViewModelContext, state: SpotifyDashboardViewState
        ): SpotifyDashboardViewModel {
            val getCategories: GetCategories by viewModelContext.activity.inject()
            val getFeaturedPlaylists: GetFeaturedPlaylists by viewModelContext.activity.inject()
            val getNewReleases: GetNewReleases by viewModelContext.activity.inject()
            val getDailyViralTracks: GetDailyViralTracks by viewModelContext.activity.inject()
            return SpotifyDashboardViewModel(state, getCategories, getFeaturedPlaylists, getNewReleases, getDailyViralTracks)
        }
    }
}