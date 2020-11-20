package com.clipfinder.spotify.dashboard

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.spotify.usecase.GetCategories
import com.clipfinder.core.spotify.usecase.GetDailyViralTracks
import com.clipfinder.core.spotify.usecase.GetFeaturedPlaylists
import com.clipfinder.core.spotify.usecase.GetNewReleases
import com.example.core.SpotifyDefaults
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.model.PagedItemsList
import com.example.core.android.spotify.model.*
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.android.util.ext.offset
import com.example.core.android.util.ext.retryLoadItemsOnNetworkAvailable2
import com.example.core.ext.map
import com.example.core.ext.mapData
import com.example.core.ext.mapIndexed
import com.example.core.model.Paged
import com.example.core.model.Resource
import io.reactivex.Observable
import io.reactivex.Single
import org.koin.android.ext.android.get

private typealias State = SpotifyDashboardState

class SpotifyDashboardViewModel(
    initialState: SpotifyDashboardState,
    private val getCategories: GetCategories,
    private val getFeaturedPlaylists: GetFeaturedPlaylists,
    private val getNewReleases: GetNewReleases,
    private val getDailyViralTracks: GetDailyViralTracks,
    private val preferences: SpotifyPreferences,
    context: Context
) : MvRxViewModel<SpotifyDashboardState>(initialState) {

    init {
        loadCategories()
        loadFeaturedPlaylists()
        loadViralTracks()
        loadNewReleases()
        handlePreferencesChanges()
        handleConnectivityChanges(context)
    }

    fun loadCategories() {
        loadPagedNoDefault(State::categories, getCategories::intoState, ::PagedItemsList) {
            copy(categories = it)
        }
    }

    fun loadFeaturedPlaylists() {
        loadPagedNoDefault(State::featuredPlaylists, getFeaturedPlaylists::intoState, ::PagedItemsList) {
            copy(featuredPlaylists = it)
        }
    }

    fun loadViralTracks() {
        loadPagedNoDefault(State::viralTracks, getDailyViralTracks::intoState, ::PagedItemsList) {
            copy(viralTracks = it)
        }
    }

    fun loadNewReleases() {
        loadPagedNoDefault(State::newReleases, getNewReleases::intoState, ::PagedItemsList) {
            copy(newReleases = it)
        }
    }

    fun clearCategoriesError() = clearErrorIn(State::categories) { copy(categories = it) }
    fun clearFeaturedPlaylistsError() = clearErrorIn(State::featuredPlaylists) { copy(featuredPlaylists = it) }
    fun clearViralTracksError() = clearErrorIn(State::viralTracks) { copy(viralTracks = it) }
    fun clearNewReleasesError() = clearErrorIn(State::newReleases) { copy(newReleases = it) }

    private fun handlePreferencesChanges() {
        Observable
            .merge(
                preferences.countryObservable.skip(1).distinctUntilChanged(),
                preferences.localeObservable.skip(1).distinctUntilChanged()
            )
            .doOnNext {
                loadCategories()
                loadFeaturedPlaylists()
            }
            .subscribe()
            .disposeOnClear()
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context.handleConnectivityChanges { (categories, playlists, tracks, releases) ->
            if (categories.retryLoadItemsOnNetworkAvailable2) loadCategories()
            if (playlists.retryLoadItemsOnNetworkAvailable2) loadFeaturedPlaylists()
            if (tracks.retryLoadItemsOnNetworkAvailable2) loadViralTracks()
            if (releases.retryLoadItemsOnNetworkAvailable2) loadNewReleases()
        }
    }

    companion object : MvRxViewModelFactory<SpotifyDashboardViewModel, State> {
        override fun create(
            viewModelContext: ViewModelContext, state: State
        ): SpotifyDashboardViewModel = SpotifyDashboardViewModel(
            state,
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.app()
        )
    }
}

private fun GetCategories.intoState(state: State): Single<Resource<Paged<List<Category>>>> =
    this(applySchedulers = false, args = state.categories.offset)
        .mapData { categories -> categories.map(::Category) }

private fun GetFeaturedPlaylists.intoState(state: State): Single<Resource<Paged<List<Playlist>>>> =
    this(applySchedulers = false, args = state.featuredPlaylists.offset)
        .mapData { playlists -> playlists.map(::Playlist) }

private fun GetNewReleases.intoState(state: State): Single<Resource<Paged<List<Album>>>> =
    this(applySchedulers = false, args = state.newReleases.offset)
        .mapData { album -> album.map(::Album) }

private fun GetDailyViralTracks.intoState(state: State): Single<Resource<Paged<List<TopTrack>>>> =
    this(applySchedulers = false, args = state.viralTracks.offset)
        .mapData { tracks ->
            tracks.mapIndexed { index, track ->
                TopTrack(
                    position = SpotifyDefaults.LIMIT * state.viralTracks.offset + index + 1,
                    track = Track(track)
                )
            }
        }
