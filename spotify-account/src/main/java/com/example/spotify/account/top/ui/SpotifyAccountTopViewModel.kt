package com.example.spotify.account.top.ui

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.mapper.spotify.ui
import com.example.core.android.model.Initial
import com.example.core.android.model.isEmptyAndLastLoadingFailedWithNetworkError
import com.example.core.android.util.ext.observeNetworkConnectivity
import com.example.core.model.map
import com.example.core.model.mapData
import com.example.spotify.account.top.domain.usecase.GetCurrentUsersTopArtists
import com.example.spotify.account.top.domain.usecase.GetCurrentUsersTopTracks
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.entity.spotify.TrackEntity
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class SpotifyAccountTopViewModel(
    initialState: SpotifyAccountTopState,
    private val getCurrentUsersTopTracks: GetCurrentUsersTopTracks,
    private val getCurrentUsersTopArtists: GetCurrentUsersTopArtists,
    context: Context
) : MvRxViewModel<SpotifyAccountTopState>(initialState) {

    init {
        handleConnectivityChanges(context)
        subscribe { (userLoggedIn, tracks, artists) ->
            if (!userLoggedIn) return@subscribe
            if (tracks.status is Initial) loadTracks()
            if (artists.status is Initial) loadArtists()
        }.disposeOnClear()
    }

    fun setUserLoggedIn(userLoggedIn: Boolean) = setState { copy(userLoggedIn = userLoggedIn) }

    fun loadTracks() = withState { (userLoggedIn, tracks) ->
        if (userLoggedIn) tracks.ifNotLoadingAndNotAllLoaded {
            getCurrentUsersTopTracks(applySchedulers = false, args = tracks.offset)
                .mapData { tracks -> tracks.map(TrackEntity::ui) }
                .subscribeOn(Schedulers.io())
                .updateWithPagedResource(SpotifyAccountTopState::topTracks) { copy(topTracks = it) }
        }
    }

    fun loadArtists() = withState { (userLoggedIn, _, artists) ->
        if (userLoggedIn) artists.ifNotLoadingAndNotAllLoaded {
            getCurrentUsersTopArtists(applySchedulers = false, args = artists.offset)
                .mapData { artists -> artists.map(ArtistEntity::ui) }
                .subscribeOn(Schedulers.io())
                .updateWithPagedResource(SpotifyAccountTopState::artists) { copy(artists = it) }
        }
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context
            .observeNetworkConnectivity {
                withState { (userLoggedIn, tracks, artists) ->
                    if (userLoggedIn && tracks.isEmptyAndLastLoadingFailedWithNetworkError()) {
                        loadTracks()
                    }
                    if (userLoggedIn && artists.isEmptyAndLastLoadingFailedWithNetworkError()) {
                        loadArtists()
                    }
                }
            }
            .disposeOnClear()
    }

    companion object : MvRxViewModelFactory<SpotifyAccountTopViewModel, SpotifyAccountTopState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: SpotifyAccountTopState
        ): SpotifyAccountTopViewModel {
            val getCurrentUsersTopTracks: GetCurrentUsersTopTracks by viewModelContext.activity.inject()
            val getCurrentUsersTopArtists: GetCurrentUsersTopArtists by viewModelContext.activity.inject()
            return SpotifyAccountTopViewModel(
                state,
                getCurrentUsersTopTracks,
                getCurrentUsersTopArtists,
                viewModelContext.app()
            )
        }
    }
}
