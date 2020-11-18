package com.example.spotify.account.top.ui

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.model.Initial
import com.example.core.android.model.retryLoadItemsOnNetworkAvailable
import com.example.core.android.spotify.model.Artist
import com.example.core.android.spotify.model.Track
import com.example.core.ext.map
import com.example.core.ext.mapData
import com.example.spotify.account.top.domain.usecase.GetCurrentUsersTopArtists
import com.example.spotify.account.top.domain.usecase.GetCurrentUsersTopTracks
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.get

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
        if (userLoggedIn && tracks.shouldLoadMore) {
            getCurrentUsersTopTracks(applySchedulers = false, args = tracks.offset)
                .mapData { newTracks -> newTracks.map(::Track) }
                .subscribeOn(Schedulers.io())
                .updateWithPagedResource(SpotifyAccountTopState::topTracks) { copy(topTracks = it) }
        }
    }

    fun loadArtists() = withState { (userLoggedIn, _, artists) ->
        if (userLoggedIn && artists.shouldLoadMore) {
            getCurrentUsersTopArtists(applySchedulers = false, args = artists.offset)
                .mapData { newArtists -> newArtists.map(::Artist) }
                .subscribeOn(Schedulers.io())
                .updateWithPagedResource(SpotifyAccountTopState::artists) { copy(artists = it) }
        }
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context.handleConnectivityChanges { (userLoggedIn, tracks, artists) ->
            if (userLoggedIn && tracks.retryLoadItemsOnNetworkAvailable) loadTracks()
            if (userLoggedIn && artists.retryLoadItemsOnNetworkAvailable) loadArtists()
        }
    }

    companion object : MvRxViewModelFactory<SpotifyAccountTopViewModel, SpotifyAccountTopState> {
        override fun create(
            viewModelContext: ViewModelContext, state: SpotifyAccountTopState
        ): SpotifyAccountTopViewModel = SpotifyAccountTopViewModel(
            state,
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.app()
        )
    }
}
