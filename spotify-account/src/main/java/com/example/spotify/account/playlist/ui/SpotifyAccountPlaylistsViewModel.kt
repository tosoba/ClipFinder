package com.example.spotify.account.playlist.ui

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.model.Initial
import com.example.core.android.model.retryLoadItemsOnNetworkAvailable
import com.example.core.android.spotify.model.Playlist
import com.example.core.android.util.ext.observeNetworkConnectivity
import com.example.core.model.map
import com.example.core.model.mapData
import com.example.spotify.account.playlist.domain.usecase.GetCurrentUsersPlaylists
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class SpotifyAccountPlaylistsViewModel(
    initialState: SpotifyAccountPlaylistState,
    private val getCurrentUsersPlaylists: GetCurrentUsersPlaylists,
    context: Context
) : MvRxViewModel<SpotifyAccountPlaylistState>(initialState) {

    init {
        handleConnectivityChanges(context)
        subscribe { (userLoggedIn, playlists) ->
            if (!userLoggedIn) return@subscribe
            if (playlists.status is Initial) loadPlaylists()
        }.disposeOnClear()
    }

    fun setUserLoggedIn(userLoggedIn: Boolean) = setState { copy(userLoggedIn = userLoggedIn) }

    fun loadPlaylists() = withState { (userLoggedIn, playlists) ->
        if (userLoggedIn && playlists.shouldLoad) {
            getCurrentUsersPlaylists(applySchedulers = false, args = playlists.offset)
                .mapData { newPlaylists -> newPlaylists.map { Playlist(it) } }
                .subscribeOn(Schedulers.io())
                .updateWithPagedResource(SpotifyAccountPlaylistState::playlists) { copy(playlists = it) }
        }
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context
            .observeNetworkConnectivity {
                withState { (userLoggedIn, playlists) ->
                    if (userLoggedIn && playlists.retryLoadItemsOnNetworkAvailable) {
                        loadPlaylists()
                    }
                }
            }
            .disposeOnClear()
    }

    companion object : MvRxViewModelFactory<SpotifyAccountPlaylistsViewModel, SpotifyAccountPlaylistState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: SpotifyAccountPlaylistState
        ): SpotifyAccountPlaylistsViewModel {
            val getCurrentUsersPlaylists: GetCurrentUsersPlaylists by viewModelContext.activity.inject()
            return SpotifyAccountPlaylistsViewModel(state, getCurrentUsersPlaylists, viewModelContext.app())
        }
    }
}