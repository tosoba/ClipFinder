package com.clipfinder.spotify.account.playlist

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.ext.map
import com.clipfinder.core.ext.mapData
import com.clipfinder.core.model.Paged
import com.clipfinder.core.model.Resource
import com.clipfinder.core.spotify.usecase.GetCurrentUsersPlaylists
import com.clipfinder.core.android.base.viewmodel.MvRxViewModel
import com.clipfinder.core.model.Empty
import com.clipfinder.core.model.Loadable
import com.clipfinder.core.model.PagedList
import com.clipfinder.core.android.spotify.model.Playlist
import com.clipfinder.core.android.spotify.preferences.SpotifyPreferences
import com.clipfinder.core.android.util.ext.offset
import com.clipfinder.core.android.util.ext.retryLoadCollectionOnConnected
import com.clipfinder.core.model.invoke
import io.reactivex.Single
import org.koin.android.ext.android.get
import kotlin.reflect.KProperty1

private typealias State = SpotifyAccountPlaylistState

class SpotifyAccountPlaylistsViewModel(
    initialState: State,
    private val getCurrentUsersPlaylists: GetCurrentUsersPlaylists,
    preferences: SpotifyPreferences,
    context: Context
) : MvRxViewModel<State>(initialState) {

    init {
        handleConnectivityChanges(context)
        preferences.isPrivateAuthorized
            .subscribe {
                setState { copy(userLoggedIn = it) }
                if (it) withState { (_, playlists) ->
                    if (playlists is Empty) loadPlaylists()
                }
            }
            .disposeOnClear()
    }

    fun loadPlaylists() = loadPagedList(State::playlists, getCurrentUsersPlaylists::intoState) { copy(playlists = it) }
    fun clearPlaylistsError() = clearErrorIn(State::playlists) { copy(playlists = it) }

    private fun <I> loadPagedList(
        prop: KProperty1<State, Loadable<PagedList<I>>>,
        action: (State) -> Single<Resource<Paged<List<I>>>>,
        reducer: State.(Loadable<PagedList<I>>) -> State
    ) = withState { state ->
        if (!state.userLoggedIn) return@withState
        loadPagedWith(state, prop, action, ::PagedList, reducer = reducer)
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context.handleConnectivityChanges { (userLoggedIn, playlists) ->
            if (userLoggedIn && playlists.retryLoadCollectionOnConnected) loadPlaylists()
        }
    }

    companion object : MvRxViewModelFactory<SpotifyAccountPlaylistsViewModel, State> {
        override fun create(
            viewModelContext: ViewModelContext, state: State
        ): SpotifyAccountPlaylistsViewModel = SpotifyAccountPlaylistsViewModel(
            state,
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.app()
        )
    }
}

internal fun GetCurrentUsersPlaylists.intoState(state: State): Single<Resource<Paged<List<Playlist>>>> =
    this(args = state.playlists.offset)
        .mapData { newPlaylists -> newPlaylists.map(::Playlist) }
