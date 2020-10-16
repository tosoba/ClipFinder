package com.example.spotify.dashboard.ui

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.SpotifyDefaults
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.model.retryLoadItemsOnNetworkAvailable
import com.example.core.android.spotify.model.*
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.model.*
import com.example.spotify.dashboard.domain.usecase.GetCategories
import com.example.spotify.dashboard.domain.usecase.GetDailyViralTracks
import com.example.spotify.dashboard.domain.usecase.GetFeaturedPlaylists
import com.example.spotify.dashboard.domain.usecase.GetNewReleases
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
        loadDailyViralTracks()
        loadNewReleases()
        handlePreferencesChanges()
        handleConnectivityChanges(context)
    }

    fun loadCategories() {
        load(State::categories, getCategories::intoState) { copy(categories = it) }
    }

    fun loadFeaturedPlaylists() {
        load(State::featuredPlaylists, getFeaturedPlaylists::intoState) { copy(featuredPlaylists = it) }
    }

    fun loadDailyViralTracks() {
        load(State::topTracks, getDailyViralTracks::intoState) { copy(topTracks = it) }
    }

    fun loadNewReleases() {
        load(State::newReleases, getNewReleases::intoState) { copy(newReleases = it) }
    }

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
            if (categories.retryLoadItemsOnNetworkAvailable) loadCategories()
            if (playlists.retryLoadItemsOnNetworkAvailable) loadFeaturedPlaylists()
            if (tracks.retryLoadItemsOnNetworkAvailable) loadDailyViralTracks()
            if (releases.retryLoadItemsOnNetworkAvailable) loadNewReleases()
        }
    }

    companion object : MvRxViewModelFactory<SpotifyDashboardViewModel, SpotifyDashboardState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: SpotifyDashboardState
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

private fun GetCategories.intoState(state: SpotifyDashboardState): Single<Resource<Paged<List<Category>>>> =
    this(applySchedulers = false, args = state.categories.offset)
        .mapData { categories -> categories.map { Category(it) } }

private fun GetFeaturedPlaylists.intoState(state: SpotifyDashboardState): Single<Resource<Paged<List<Playlist>>>> =
    this(applySchedulers = false, args = state.featuredPlaylists.offset)
        .mapData { playlists -> playlists.map { Playlist(it) } }

private fun GetNewReleases.intoState(state: SpotifyDashboardState): Single<Resource<Paged<List<Album>>>> =
    this(applySchedulers = false, args = state.newReleases.offset)
        .mapData { album -> album.map { Album(it) } }

private fun GetDailyViralTracks.intoState(state: SpotifyDashboardState): Single<Resource<Paged<List<TopTrack>>>> =
    this(applySchedulers = false, args = state.topTracks.offset)
        .mapData { tracks ->
            tracks.mapIndexed { index, track ->
                TopTrack(
                    position = SpotifyDefaults.LIMIT * state.topTracks.offset + index + 1,
                    track = Track(track)
                )
            }
        }
