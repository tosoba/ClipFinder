package com.example.spotify.dashboard.ui

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.SpotifyDefaults
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.model.isEmptyAndLastLoadingFailedWithNetworkError
import com.example.core.android.spotify.model.*
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.android.util.ext.observeNetworkConnectivity
import com.example.core.model.*
import com.example.spotify.dashboard.domain.usecase.GetCategories
import com.example.spotify.dashboard.domain.usecase.GetDailyViralTracks
import com.example.spotify.dashboard.domain.usecase.GetFeaturedPlaylists
import com.example.spotify.dashboard.domain.usecase.GetNewReleases
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.get

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

    fun loadCategories() = withState { (categories) ->
        if (categories.shouldLoad) {
            getCategories(applySchedulers = false, args = categories.offset)
                .mapData { newCategories -> newCategories.map { Category(it) } }
                .subscribeOn(Schedulers.io())
                .updateWithPagedResource(SpotifyDashboardState::categories) {
                    copy(categories = it)
                }
        }
    }

    fun loadFeaturedPlaylists() = withState { (_, featuredPlaylists) ->
        if (featuredPlaylists.shouldLoad) {
            getFeaturedPlaylists(applySchedulers = false, args = featuredPlaylists.offset)
                .mapData { playlists -> playlists.map { Playlist(it) } }
                .subscribeOn(Schedulers.io())
                .updateWithPagedResource(SpotifyDashboardState::featuredPlaylists) {
                    copy(featuredPlaylists = it)
                }
        }
    }

    fun loadDailyViralTracks() =
        load(SpotifyDashboardState::topTracks, getDailyViralTracks::intoState) { copy(topTracks = it) }

    fun loadNewReleases() = withState { (_, _, _, newReleases) ->
        if (newReleases.shouldLoad) {
            getNewReleases(applySchedulers = false, args = newReleases.offset)
                .mapData { listPage -> listPage.map { Album(it) } }
                .subscribeOn(Schedulers.io())
                .updateWithPagedResource(SpotifyDashboardState::newReleases) {
                    copy(newReleases = it)
                }
        }
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
        context
            .observeNetworkConnectivity {
                withState { (categories, playlists, tracks, releases) ->
                    if (categories.isEmptyAndLastLoadingFailedWithNetworkError()) loadCategories()
                    if (playlists.isEmptyAndLastLoadingFailedWithNetworkError()) loadFeaturedPlaylists()
                    if (tracks.isEmptyAndLastLoadingFailedWithNetworkError()) loadDailyViralTracks()
                    if (releases.isEmptyAndLastLoadingFailedWithNetworkError()) loadNewReleases()
                }
            }
            .disposeOnClear()
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
