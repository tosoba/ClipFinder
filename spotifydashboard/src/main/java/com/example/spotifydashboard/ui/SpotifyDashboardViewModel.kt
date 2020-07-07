package com.example.spotifydashboard.ui

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.model.mapData
import com.example.coreandroid.base.vm.MvRxViewModel
import com.example.coreandroid.mapper.spotify.ui
import com.example.coreandroid.model.Loading
import com.example.coreandroid.model.spotify.TopTrack
import com.example.spotifydashboard.domain.usecase.GetCategories
import com.example.spotifydashboard.domain.usecase.GetDailyViralTracks
import com.example.spotifydashboard.domain.usecase.GetFeaturedPlaylists
import com.example.spotifydashboard.domain.usecase.GetNewReleases
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.CategoryEntity
import com.example.there.domain.entity.spotify.PlaylistEntity
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class SpotifyDashboardViewModel(
    initialState: SpotifyDashboardState,
    private val getCategories: GetCategories,
    private val getFeaturedPlaylists: GetFeaturedPlaylists,
    private val getNewReleases: GetNewReleases,
    private val getDailyViralTracks: GetDailyViralTracks
) : MvRxViewModel<SpotifyDashboardState>(initialState) {

    init {
        loadCategories()
        loadFeaturedPlaylists()
        loadDailyViralTracks()
        loadNewReleases()
    }

    fun loadCategories() = withState { state ->
        if (state.categories.status is Loading) return@withState

        getCategories(applySchedulers = false)
            .mapData { categories ->
                categories.map(CategoryEntity::ui).sortedBy { it.name }
            }
            .subscribeOn(Schedulers.io())
            .updateWithResource(SpotifyDashboardState::categories) {
                copy(categories = it)
            }
    }


    fun loadFeaturedPlaylists() = withState { state ->
        if (state.featuredPlaylists.status is Loading) return@withState

        getFeaturedPlaylists(applySchedulers = false)
            .mapData { playlists ->
                playlists.map(PlaylistEntity::ui).sortedBy { it.name }
            }
            .subscribeOn(Schedulers.io())
            .updateWithResource(SpotifyDashboardState::featuredPlaylists) {
                copy(featuredPlaylists = it)
            }
    }

    fun loadDailyViralTracks() = withState { state ->
        if (state.topTracks.status is Loading) return@withState

        getDailyViralTracks(applySchedulers = false)
            .timeout(10, TimeUnit.SECONDS)
            .mapData { tracks ->
                tracks.map { TopTrack(it.position, it.track.ui) }
                    .sortedBy { it.position }
            }
            .subscribeOn(Schedulers.io())
            .updateWithResource(SpotifyDashboardState::topTracks) { copy(topTracks = it) }
    }

    fun loadNewReleases() = withState { state ->
        state.newReleases.ifNotLoadingAndNotAllLoaded {
            getNewReleases(applySchedulers = false, args = current { newReleases.offset })
                .mapData { listPage -> listPage.map(AlbumEntity::ui) }
                .subscribeOn(Schedulers.io())
                .updateWithPagedResource(SpotifyDashboardState::newReleases) {
                    copy(newReleases = it)
                }
        }
    }

    companion object : MvRxViewModelFactory<SpotifyDashboardViewModel, SpotifyDashboardState> {
        override fun create(
            viewModelContext: ViewModelContext, state: SpotifyDashboardState
        ): SpotifyDashboardViewModel {
            val getCategories: GetCategories by viewModelContext.activity.inject()
            val getFeaturedPlaylists: GetFeaturedPlaylists by viewModelContext.activity.inject()
            val getNewReleases: GetNewReleases by viewModelContext.activity.inject()
            val getDailyViralTracks: GetDailyViralTracks by viewModelContext.activity.inject()
            return SpotifyDashboardViewModel(state, getCategories, getFeaturedPlaylists, getNewReleases, getDailyViralTracks)
        }
    }
}