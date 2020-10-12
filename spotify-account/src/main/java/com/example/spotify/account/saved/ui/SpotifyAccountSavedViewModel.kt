package com.example.spotify.account.saved.ui

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.model.Initial
import com.example.core.android.model.shouldLoadOnNetworkAvailable
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Track
import com.example.core.android.util.ext.observeNetworkConnectivity
import com.example.core.model.map
import com.example.core.model.mapData
import com.example.spotify.account.saved.domain.usecase.GetCurrentUsersSavedAlbums
import com.example.spotify.account.saved.domain.usecase.GetCurrentUsersSavedTracks
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class SpotifyAccountSavedViewModel(
    initialState: SpotifyAccountSavedState,
    private val getCurrentUsersSavedTracks: GetCurrentUsersSavedTracks,
    private val getCurrentUsersSavedAlbums: GetCurrentUsersSavedAlbums,
    context: Context
) : MvRxViewModel<SpotifyAccountSavedState>(initialState) {

    init {
        handleConnectivityChanges(context)
        subscribe { (userLoggedIn, tracks, albums) ->
            if (!userLoggedIn) return@subscribe
            if (tracks.status is Initial) loadTracks()
            if (albums.status is Initial) loadAlbums()
        }.disposeOnClear()
    }

    fun setUserLoggedIn(userLoggedIn: Boolean) = setState { copy(userLoggedIn = userLoggedIn) }

    fun loadTracks() = withState { (userLoggedIn, tracks) ->
        if (userLoggedIn && tracks.shouldLoad) {
            getCurrentUsersSavedTracks(applySchedulers = false, args = tracks.offset)
                .mapData { newTracks -> newTracks.map { Track(it) } }
                .subscribeOn(Schedulers.io())
                .updateWithPagedResource(SpotifyAccountSavedState::tracks) { copy(tracks = it) }
        }
    }

    fun loadAlbums() = withState { (userLoggedIn, _, albums) ->
        if (userLoggedIn && albums.shouldLoad) {
            getCurrentUsersSavedAlbums(applySchedulers = false, args = albums.offset)
                .mapData { newAlbums -> newAlbums.map { Album(it) } }
                .subscribeOn(Schedulers.io())
                .updateWithPagedResource(SpotifyAccountSavedState::albums) { copy(albums = it) }
        }
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context
            .observeNetworkConnectivity {
                withState { (userLoggedIn, tracks, albums) ->
                    if (userLoggedIn && tracks.shouldLoadOnNetworkAvailable()) {
                        loadTracks()
                    }
                    if (userLoggedIn && albums.shouldLoadOnNetworkAvailable()) {
                        loadAlbums()
                    }
                }
            }
            .disposeOnClear()
    }


    companion object : MvRxViewModelFactory<SpotifyAccountSavedViewModel, SpotifyAccountSavedState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: SpotifyAccountSavedState
        ): SpotifyAccountSavedViewModel {
            val getCurrentUsersSavedTracks: GetCurrentUsersSavedTracks by viewModelContext.activity.inject()
            val getCurrentUsersSavedAlbums: GetCurrentUsersSavedAlbums by viewModelContext.activity.inject()
            return SpotifyAccountSavedViewModel(
                state,
                getCurrentUsersSavedTracks,
                getCurrentUsersSavedAlbums,
                viewModelContext.app()
            )
        }
    }
}
