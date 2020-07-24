package com.example.spotify.dashboard.ui

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.model.map
import com.example.core.model.mapData
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.mapper.spotify.ui
import com.example.core.android.model.isEmptyAndLastLoadingFailedWithNetworkError
import com.example.core.android.model.spotify.TopTrack
import com.example.core.android.util.ext.observeNetworkConnectivity
import com.example.spotify.dashboard.domain.usecase.GetCategories
import com.example.spotify.dashboard.domain.usecase.GetDailyViralTracks
import com.example.spotify.dashboard.domain.usecase.GetFeaturedPlaylists
import com.example.spotify.dashboard.domain.usecase.GetNewReleases
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.CategoryEntity
import com.example.there.domain.entity.spotify.PlaylistEntity
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

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
        categories.ifNotLoadingAndNotAllLoaded {
            getCategories(applySchedulers = false, args = categories.offset)
                .mapData { categories -> categories.map(CategoryEntity::ui) }
                .subscribeOn(Schedulers.io())
                .updateWithPagedResource(SpotifyDashboardState::categories) {
                    copy(categories = it)
                }
        }
    }

    fun loadFeaturedPlaylists() = withState { (_, featuredPlaylists) ->
        featuredPlaylists.ifNotLoadingAndNotAllLoaded {
            getFeaturedPlaylists(applySchedulers = false, args = featuredPlaylists.offset)
                .mapData { playlists -> playlists.map(PlaylistEntity::ui) }
                .subscribeOn(Schedulers.io())
                .updateWithPagedResource(SpotifyDashboardState::featuredPlaylists) {
                    copy(featuredPlaylists = it)
                }
        }
    }

    fun loadDailyViralTracks() = withState { (_, _, topTracks) ->
        topTracks.ifNotLoadingAndNotAllLoaded {
            getDailyViralTracks(applySchedulers = false, args = topTracks.offset)
                .mapData { tracks -> tracks.map { TopTrack(it.position, it.track.ui) } }
                .subscribeOn(Schedulers.io())
                .updateWithPagedResource(SpotifyDashboardState::topTracks) { copy(topTracks = it) }
        }
    }

    fun loadNewReleases() = withState { (_, _, _, newReleases) ->
        newReleases.ifNotLoadingAndNotAllLoaded {
            getNewReleases(applySchedulers = false, args = newReleases.offset)
                .mapData { listPage -> listPage.map(AlbumEntity::ui) }
                .subscribeOn(Schedulers.io())
                .updateWithPagedResource(SpotifyDashboardState::newReleases) {
                    copy(newReleases = it)
                }
        }
    }

    private fun handlePreferencesChanges() {
        Observable.merge(
            preferences.countryObservable.skip(1).distinctUntilChanged(),
            preferences.localeObservable.skip(1).distinctUntilChanged()
        ).doOnNext {
            loadCategories()
            loadFeaturedPlaylists()
        }.subscribe().disposeOnClear()
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context.observeNetworkConnectivity {
            withState { (categories, playlists, tracks, releases) ->
                if (categories.isEmptyAndLastLoadingFailedWithNetworkError()) loadCategories()
                if (playlists.isEmptyAndLastLoadingFailedWithNetworkError()) loadFeaturedPlaylists()
                if (tracks.isEmptyAndLastLoadingFailedWithNetworkError()) loadDailyViralTracks()
                if (releases.isEmptyAndLastLoadingFailedWithNetworkError()) loadNewReleases()
            }
        }.disposeOnClear()
    }

    companion object : MvRxViewModelFactory<SpotifyDashboardViewModel, SpotifyDashboardState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: SpotifyDashboardState
        ): SpotifyDashboardViewModel {
            val getCategories: GetCategories by viewModelContext.activity.inject()
            val getFeaturedPlaylists: GetFeaturedPlaylists by viewModelContext.activity.inject()
            val getNewReleases: GetNewReleases by viewModelContext.activity.inject()
            val getDailyViralTracks: GetDailyViralTracks by viewModelContext.activity.inject()
            val preferences: SpotifyPreferences by viewModelContext.activity.inject()
            return SpotifyDashboardViewModel(
                state,
                getCategories,
                getFeaturedPlaylists,
                getNewReleases,
                getDailyViralTracks,
                preferences,
                viewModelContext.app()
            )
        }
    }
}
