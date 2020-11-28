package com.clipfinder.spotify.account.saved

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.ext.map
import com.clipfinder.core.ext.mapData
import com.clipfinder.core.model.Paged
import com.clipfinder.core.model.Resource
import com.clipfinder.core.spotify.usecase.GetCurrentUsersSavedAlbums
import com.clipfinder.core.spotify.usecase.GetCurrentUsersSavedTracks
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.model.Empty
import com.example.core.android.model.Loadable
import com.example.core.android.model.PagedList
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Track
import com.example.core.android.util.ext.offset
import com.example.core.android.util.ext.retryLoadCollectionOnConnected
import io.reactivex.Single
import org.koin.android.ext.android.get
import kotlin.reflect.KProperty1

private typealias State = SpotifyAccountSavedState

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
            if (tracks is Empty) loadTracks()
            if (albums is Empty) loadAlbums()
        }.disposeOnClear()
    }

    fun setUserLoggedIn(userLoggedIn: Boolean) = setState { copy(userLoggedIn = userLoggedIn) }
    fun loadTracks() = loadPagedList(State::tracks, getCurrentUsersSavedTracks::intoState) { copy(tracks = it) }
    fun clearTracksError() = clearErrorIn(State::tracks) { copy(tracks = it) }
    fun loadAlbums() = loadPagedList(State::albums, getCurrentUsersSavedAlbums::intoState) { copy(albums = it) }
    fun clearAlbumsError() = clearErrorIn(State::albums) { copy(albums = it) }

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
        context.handleConnectivityChanges { (userLoggedIn, tracks, albums) ->
            if (userLoggedIn && tracks.retryLoadCollectionOnConnected) loadTracks()
            if (userLoggedIn && albums.retryLoadCollectionOnConnected) loadAlbums()
        }
    }

    companion object : MvRxViewModelFactory<SpotifyAccountSavedViewModel, SpotifyAccountSavedState> {
        override fun create(
            viewModelContext: ViewModelContext, state: SpotifyAccountSavedState
        ): SpotifyAccountSavedViewModel = SpotifyAccountSavedViewModel(
            state,
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.app()
        )
    }
}

internal fun GetCurrentUsersSavedTracks.intoState(
    state: State
): Single<Resource<Paged<List<Track>>>> = this(applySchedulers = false, args = state.tracks.offset)
    .mapData { newTracks -> newTracks.map(::Track) }

internal fun GetCurrentUsersSavedAlbums.intoState(
    state: State
): Single<Resource<Paged<List<Album>>>> = this(applySchedulers = false, args = state.albums.offset)
    .mapData { newAlbums -> newAlbums.map(::Album) }
