package com.clipfinder.spotify.dashboard

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.SpotifyDefaults
import com.clipfinder.core.ext.map
import com.clipfinder.core.ext.mapData
import com.clipfinder.core.ext.mapIndexed
import com.clipfinder.core.model.Paged
import com.clipfinder.core.model.Resource
import com.clipfinder.core.spotify.usecase.GetCategories
import com.clipfinder.core.spotify.usecase.GetDailyViralTracks
import com.clipfinder.core.spotify.usecase.GetFeaturedPlaylists
import com.clipfinder.core.spotify.usecase.GetNewReleases
import com.clipfinder.core.android.base.viewmodel.MvRxViewModel
import com.clipfinder.core.model.PagedList
import com.clipfinder.core.android.spotify.model.*
import com.clipfinder.core.android.spotify.preferences.SpotifyPreferences
import com.clipfinder.core.android.util.ext.offset
import com.clipfinder.core.android.util.ext.retryLoadCollectionOnConnected
import com.clipfinder.core.model.invoke
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

    fun loadCategories() = loadPaged(State::categories, getCategories::intoState, ::PagedList) { copy(categories = it) }
    fun loadFeaturedPlaylists() = loadPaged(State::featuredPlaylists, getFeaturedPlaylists::intoState, ::PagedList) { copy(featuredPlaylists = it) }
    fun loadViralTracks() = loadPaged(State::viralTracks, getDailyViralTracks::intoState, ::PagedList) { copy(viralTracks = it) }
    fun loadNewReleases() = loadPaged(State::newReleases, getNewReleases::intoState, ::PagedList) { copy(newReleases = it) }
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
            if (categories.retryLoadCollectionOnConnected) loadCategories()
            if (playlists.retryLoadCollectionOnConnected) loadFeaturedPlaylists()
            if (tracks.retryLoadCollectionOnConnected) loadViralTracks()
            if (releases.retryLoadCollectionOnConnected) loadNewReleases()
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
    this(args = state.categories.offset)
        .mapData { categories -> categories.map(::Category) }

private fun GetFeaturedPlaylists.intoState(state: State): Single<Resource<Paged<List<Playlist>>>> =
    this(args = state.featuredPlaylists.offset)
        .mapData { playlists -> playlists.map(::Playlist) }

private fun GetNewReleases.intoState(state: State): Single<Resource<Paged<List<Album>>>> =
    this(args = state.newReleases.offset)
        .mapData { albums -> albums.map(::Album) }

private fun GetDailyViralTracks.intoState(state: State): Single<Resource<Paged<List<TopTrack>>>> =
    this(args = state.viralTracks.offset)
        .mapData { tracks ->
            tracks.mapIndexed { index, track ->
                TopTrack(
                    position = SpotifyDefaults.LIMIT * state.viralTracks.offset + index + 1,
                    track = Track(track)
                )
            }
        }
