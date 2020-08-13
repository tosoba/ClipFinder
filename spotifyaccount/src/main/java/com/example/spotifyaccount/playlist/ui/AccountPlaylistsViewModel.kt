package com.example.spotifyaccount.playlist.ui

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
import com.example.spotifyaccount.playlist.domain.usecase.GetCurrentUsersPlaylists
import com.example.there.domain.entity.spotify.PlaylistEntity
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class AccountPlaylistsViewModel(
    initialState: AccountPlaylistState,
    private val getCurrentUsersPlaylists: GetCurrentUsersPlaylists,
    context: Context
) : MvRxViewModel<AccountPlaylistState>(initialState) {

    init {
        handleConnectivityChanges(context)
        subscribe { (userLoggedIn, playlists) ->
            if (!userLoggedIn) return@subscribe
            if (playlists.status is Initial) loadPlaylists()
        }.disposeOnClear()
    }

    fun setUserLoggedIn(userLoggedIn: Boolean) = setState { copy(userLoggedIn = userLoggedIn) }

    fun loadPlaylists() = withState { (userLoggedIn, playlists) ->
        if (userLoggedIn) playlists.ifNotLoadingAndNotAllLoaded {
            getCurrentUsersPlaylists(applySchedulers = false, args = playlists.offset)
                .mapData { playlists -> playlists.map(PlaylistEntity::ui) }
                .subscribeOn(Schedulers.io())
                .updateWithPagedResource(AccountPlaylistState::playlists) { copy(playlists = it) }
        }
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context
            .observeNetworkConnectivity {
                withState { (userLoggedIn, playlists) ->
                    if (userLoggedIn && playlists.isEmptyAndLastLoadingFailedWithNetworkError()) {
                        loadPlaylists()
                    }
                }
            }
            .disposeOnClear()
    }

    companion object : MvRxViewModelFactory<AccountPlaylistsViewModel, AccountPlaylistState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: AccountPlaylistState
        ): AccountPlaylistsViewModel {
            val getCurrentUsersPlaylists: GetCurrentUsersPlaylists by viewModelContext.activity.inject()
            return AccountPlaylistsViewModel(state, getCurrentUsersPlaylists, viewModelContext.app())
        }
    }
}