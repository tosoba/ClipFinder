package com.example.spotifyaccount.saved.ui

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
import com.example.spotifyaccount.saved.domain.usecase.GetCurrentUsersSavedAlbums
import com.example.spotifyaccount.saved.domain.usecase.GetCurrentUsersSavedTracks
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.TrackEntity
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class AccountSavedViewModel(
    initialState: AccountSavedState,
    private val getCurrentUsersSavedTracks: GetCurrentUsersSavedTracks,
    private val getCurrentUsersSavedAlbums: GetCurrentUsersSavedAlbums,
    context: Context
) : MvRxViewModel<AccountSavedState>(initialState) {

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
        if (userLoggedIn) tracks.ifNotLoadingAndNotAllLoaded {
            getCurrentUsersSavedTracks(applySchedulers = false, args = tracks.offset)
                .mapData { tracks -> tracks.map(TrackEntity::ui) }
                .subscribeOn(Schedulers.io())
                .updateWithPagedResource(AccountSavedState::tracks) { copy(tracks = it) }
        }
    }

    fun loadAlbums() = withState { (userLoggedIn, _, albums) ->
        if (userLoggedIn) albums.ifNotLoadingAndNotAllLoaded {
            getCurrentUsersSavedAlbums(applySchedulers = false, args = albums.offset)
                .mapData { albums -> albums.map(AlbumEntity::ui) }
                .subscribeOn(Schedulers.io())
                .updateWithPagedResource(AccountSavedState::albums) { copy(albums = it) }
        }
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context
            .observeNetworkConnectivity {
                withState { (userLoggedIn, tracks, albums) ->
                    if (userLoggedIn && tracks.isEmptyAndLastLoadingFailedWithNetworkError()) {
                        loadTracks()
                    }
                    if (userLoggedIn && albums.isEmptyAndLastLoadingFailedWithNetworkError()) {
                        loadAlbums()
                    }
                }
            }
            .disposeOnClear()
    }


    companion object : MvRxViewModelFactory<AccountSavedViewModel, AccountSavedState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: AccountSavedState
        ): AccountSavedViewModel {
            val getCurrentUsersSavedTracks: GetCurrentUsersSavedTracks by viewModelContext.activity.inject()
            val getCurrentUsersSavedAlbums: GetCurrentUsersSavedAlbums by viewModelContext.activity.inject()
            return AccountSavedViewModel(
                state,
                getCurrentUsersSavedTracks,
                getCurrentUsersSavedAlbums,
                viewModelContext.app()
            )
        }
    }
}
