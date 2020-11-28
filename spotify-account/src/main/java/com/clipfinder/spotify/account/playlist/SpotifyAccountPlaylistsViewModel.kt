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
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.model.Empty
import com.example.core.android.model.Loadable
import com.example.core.android.model.PagedList
import com.example.core.android.spotify.model.Playlist
import com.example.core.android.util.ext.offset
import com.example.core.android.util.ext.retryLoadCollectionOnConnected
import io.reactivex.Single
import org.koin.android.ext.android.get
import kotlin.reflect.KProperty1

private typealias State = SpotifyAccountPlaylistState

class SpotifyAccountPlaylistsViewModel(
    initialState: State,
    private val getCurrentUsersPlaylists: GetCurrentUsersPlaylists,
    context: Context
) : MvRxViewModel<State>(initialState) {

    init {
        handleConnectivityChanges(context)
        subscribe { (userLoggedIn, playlists) ->
            if (!userLoggedIn) return@subscribe
            if (playlists is Empty) loadPlaylists()
        }.disposeOnClear()
    }

    fun setUserLoggedIn(userLoggedIn: Boolean) = setState { copy(userLoggedIn = userLoggedIn) }
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
            viewModelContext.app()
        )
    }
}

internal fun GetCurrentUsersPlaylists.intoState(
    state: State
): Single<Resource<Paged<List<Playlist>>>> = this(applySchedulers = false, args = state.playlists.offset)
    .mapData { newPlaylists -> newPlaylists.map(::Playlist) }
