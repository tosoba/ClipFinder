package com.clipfinder.spotify.account.top

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.spotify.usecase.GetCurrentUsersTopArtists
import com.clipfinder.core.spotify.usecase.GetCurrentUsersTopTracks
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.model.Empty
import com.example.core.android.model.Loadable
import com.example.core.android.model.PagedList
import com.example.core.android.spotify.model.Artist
import com.example.core.android.spotify.model.Track
import com.example.core.android.util.ext.offset
import com.example.core.android.util.ext.retryLoadCollectionOnConnected
import com.example.core.ext.map
import com.example.core.ext.mapData
import com.example.core.model.Paged
import com.example.core.model.Resource
import io.reactivex.Single
import org.koin.android.ext.android.get
import kotlin.reflect.KProperty1

private typealias State = SpotifyAccountTopState

class SpotifyAccountTopViewModel(
    initialState: State,
    private val getCurrentUsersTopTracks: GetCurrentUsersTopTracks,
    private val getCurrentUsersTopArtists: GetCurrentUsersTopArtists,
    context: Context
) : MvRxViewModel<State>(initialState) {

    init {
        handleConnectivityChanges(context)
        subscribe { (userLoggedIn, tracks, artists) ->
            if (!userLoggedIn) return@subscribe
            if (tracks is Empty) loadTracks()
            if (artists is Empty) loadArtists()
        }.disposeOnClear()
    }

    fun setUserLoggedIn(userLoggedIn: Boolean) = setState { copy(userLoggedIn = userLoggedIn) }
    fun loadTracks() = load(State::topTracks, getCurrentUsersTopTracks::intoState) { copy(topTracks = it) }
    fun clearTracksError() = clearErrorIn(State::topTracks) { copy(topTracks = it) }
    fun loadArtists() = load(State::artists, getCurrentUsersTopArtists::intoState) { copy(artists = it) }
    fun clearArtistsError() = clearErrorIn(State::artists) { copy(artists = it) }

    private fun <I> load(
        prop: KProperty1<State, Loadable<PagedList<I>>>,
        action: (State) -> Single<Resource<Paged<List<I>>>>,
        reducer: State.(Loadable<PagedList<I>>) -> State
    ) = withState { state ->
        if (!state.userLoggedIn) return@withState
        loadPagedWith(state, prop, action, ::PagedList, reducer = reducer)
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context.handleConnectivityChanges { (userLoggedIn, tracks, artists) ->
            if (userLoggedIn && tracks.retryLoadCollectionOnConnected) loadTracks()
            if (userLoggedIn && artists.retryLoadCollectionOnConnected) loadArtists()
        }
    }

    companion object : MvRxViewModelFactory<SpotifyAccountTopViewModel, State> {
        override fun create(
            viewModelContext: ViewModelContext, state: State
        ): SpotifyAccountTopViewModel = SpotifyAccountTopViewModel(
            state,
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.app()
        )
    }
}

internal fun GetCurrentUsersTopTracks.intoState(
    state: State
): Single<Resource<Paged<List<Track>>>> = this(applySchedulers = false, args = state.topTracks.offset)
    .mapData { newTracks -> newTracks.map(::Track) }

internal fun GetCurrentUsersTopArtists.intoState(
    state: State
): Single<Resource<Paged<List<Artist>>>> = this(applySchedulers = false, args = state.artists.offset)
    .mapData { newArtists -> newArtists.map(::Artist) }
