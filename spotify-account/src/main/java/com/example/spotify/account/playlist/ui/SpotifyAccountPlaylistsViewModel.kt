package com.example.spotify.account.playlist.ui

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.model.Initial
import com.example.core.android.model.retryLoadItemsOnNetworkAvailable
import com.example.core.android.spotify.model.Playlist
import com.example.core.ext.map
import com.example.core.ext.mapData
import com.example.spotify.account.playlist.domain.usecase.GetCurrentUsersPlaylists
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.get

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
        if (userLoggedIn && playlists.shouldLoadMore) {
            getCurrentUsersPlaylists(applySchedulers = false, args = playlists.offset)
                .mapData { newPlaylists -> newPlaylists.map(::Playlist) }
                .subscribeOn(Schedulers.io())
                .updateWithPagedResource(SpotifyAccountPlaylistState::playlists) { copy(playlists = it) }
        }
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context.handleConnectivityChanges { (userLoggedIn, playlists) ->
            if (userLoggedIn && playlists.retryLoadItemsOnNetworkAvailable) loadPlaylists()
        }
    }

    companion object : MvRxViewModelFactory<SpotifyAccountPlaylistsViewModel, SpotifyAccountPlaylistState> {
        override fun create(
            viewModelContext: ViewModelContext, state: SpotifyAccountPlaylistState
        ): SpotifyAccountPlaylistsViewModel = SpotifyAccountPlaylistsViewModel(
            state,
            viewModelContext.activity.get(),
            viewModelContext.app()
        )
    }
}