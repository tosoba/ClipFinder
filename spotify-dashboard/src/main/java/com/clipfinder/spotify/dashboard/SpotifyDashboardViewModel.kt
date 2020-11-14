package com.clipfinder.spotify.dashboard

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.SpotifyDefaults
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.util.ext.retryLoadItemsOnNetworkAvailable
import com.example.core.android.spotify.model.*
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.model.*
import com.clipfinder.core.spotify.usecase.GetCategories
import com.clipfinder.core.spotify.usecase.GetDailyViralTracks
import com.clipfinder.core.spotify.usecase.GetFeaturedPlaylists
import com.clipfinder.core.spotify.usecase.GetNewReleases
import com.example.core.ext.map
import com.example.core.ext.mapData
import com.example.core.ext.mapIndexed
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
        loadPaged(State::categories, getCategories::intoState) { copy(categories = it) }
    }

    fun clearCategoriesError() {
        clearError(State::categories) { copy(categories = it) }
    }

    fun loadFeaturedPlaylists() {
        loadPaged(State::featuredPlaylists, getFeaturedPlaylists::intoState) { copy(featuredPlaylists = it) }
    }

    fun clearFeaturedPlaylistsError() {
        clearError(State::featuredPlaylists) { copy(featuredPlaylists = it) }
    }

    fun loadViralTracks() {
        loadPaged(State::viralTracks, getDailyViralTracks::intoState) { copy(viralTracks = it) }
    }

    fun clearViralTracksError() {
        clearError(State::viralTracks) { copy(viralTracks = it) }
    }

    fun loadNewReleases() {
        loadPaged(State::newReleases, getNewReleases::intoState) { copy(newReleases = it) }
    }

    fun clearNewReleasesError() {
        clearError(State::newReleases) { copy(newReleases = it) }
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
            if (tracks.retryLoadItemsOnNetworkAvailable) loadViralTracks()
            if (releases.retryLoadItemsOnNetworkAvailable) loadNewReleases()
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
    this(applySchedulers = false, args = state.categories.value.offset)
        .mapData { categories -> categories.map(::Category) }

private fun GetFeaturedPlaylists.intoState(state: State): Single<Resource<Paged<List<Playlist>>>> =
    this(applySchedulers = false, args = state.featuredPlaylists.value.offset)
        .mapData { playlists -> playlists.map(::Playlist) }

private fun GetNewReleases.intoState(state: State): Single<Resource<Paged<List<Album>>>> =
    this(applySchedulers = false, args = state.newReleases.value.offset)
        .mapData { album -> album.map(::Album) }

private fun GetDailyViralTracks.intoState(state: State): Single<Resource<Paged<List<TopTrack>>>> =
    this(applySchedulers = false, args = state.viralTracks.value.offset)
        .mapData { tracks ->
            tracks.mapIndexed { index, track ->
                TopTrack(
                    position = SpotifyDefaults.LIMIT * state.viralTracks.value.offset + index + 1,
                    track = Track(track)
                )
            }
        }
