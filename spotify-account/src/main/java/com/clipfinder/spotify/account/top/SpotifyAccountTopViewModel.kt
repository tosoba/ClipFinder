package com.clipfinder.spotify.account.top

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.android.base.viewmodel.MvRxViewModel
import com.clipfinder.core.android.spotify.model.Artist
import com.clipfinder.core.android.spotify.model.Track
import com.clipfinder.core.android.spotify.preferences.SpotifyPreferences
import com.clipfinder.core.android.util.ext.offset
import com.clipfinder.core.android.util.ext.retryLoadCollectionOnConnected
import com.clipfinder.core.ext.map
import com.clipfinder.core.ext.mapData
import com.clipfinder.core.model.*
import com.clipfinder.core.spotify.usecase.GetCurrentUsersTopArtists
import com.clipfinder.core.spotify.usecase.GetCurrentUsersTopTracks
import io.reactivex.Single
import org.koin.android.ext.android.get
import kotlin.reflect.KProperty1

private typealias State = SpotifyAccountTopState

class SpotifyAccountTopViewModel(
    initialState: State,
    private val getCurrentUsersTopTracks: GetCurrentUsersTopTracks,
    private val getCurrentUsersTopArtists: GetCurrentUsersTopArtists,
    preferences: SpotifyPreferences,
    context: Context
) : MvRxViewModel<State>(initialState) {

    init {
        handleConnectivityChanges(context)
        preferences.isPrivateAuthorizedObservable
            .subscribe {
                setState { copy(userLoggedIn = it) }
                if (it)
                    withState { (_, tracks, artists) ->
                        if (tracks is Empty) loadTracks()
                        if (artists is Empty) loadArtists()
                    }
            }
            .disposeOnClear()
    }

    fun loadTracks() =
        load(State::topTracks, getCurrentUsersTopTracks::intoState) { copy(topTracks = it) }
    fun clearTracksError() = clearErrorIn(State::topTracks) { copy(topTracks = it) }
    fun loadArtists() =
        load(State::artists, getCurrentUsersTopArtists::intoState) { copy(artists = it) }
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
            viewModelContext: ViewModelContext,
            state: State
        ): SpotifyAccountTopViewModel =
            SpotifyAccountTopViewModel(
                state,
                viewModelContext.activity.get(),
                viewModelContext.activity.get(),
                viewModelContext.activity.get(),
                viewModelContext.app()
            )
    }
}

internal fun GetCurrentUsersTopTracks.intoState(
    state: State
): Single<Resource<Paged<List<Track>>>> =
    this(args = state.topTracks.offset).mapData { newTracks -> newTracks.map(::Track) }

internal fun GetCurrentUsersTopArtists.intoState(
    state: State
): Single<Resource<Paged<List<Artist>>>> =
    this(args = state.artists.offset).mapData { newArtists -> newArtists.map(::Artist) }
