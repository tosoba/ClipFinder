package com.clipfinder.spotify.playlist

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.ext.map
import com.clipfinder.core.ext.mapData
import com.clipfinder.core.spotify.usecase.GetPlaylistTracks
import com.example.core.android.base.viewmodel.MvRxViewModel
import com.clipfinder.core.model.PagedList
import com.example.core.android.spotify.model.Track
import com.example.core.android.util.ext.offset
import com.example.core.android.util.ext.retryLoadCollectionOnConnected
import org.koin.android.ext.android.get

private typealias State = SpotifyPlaylistState

class SpotifyPlaylistViewModel(
    initialState: State,
    private val getPlaylistTracks: GetPlaylistTracks,
    context: Context
) : MvRxViewModel<State>(initialState) {

    init {
        loadTracks()
        handleConnectivityChanges(context)
    }

    fun loadTracks() = loadPaged(State::tracks, getPlaylistTracks::intoState, ::PagedList) { copy(tracks = it) }
    fun clearTracksError() = clearErrorIn(State::tracks) { copy(tracks = it) }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context.handleConnectivityChanges { (_, tracks) ->
            if (tracks.retryLoadCollectionOnConnected) loadTracks()
        }
    }

    companion object : MvRxViewModelFactory<SpotifyPlaylistViewModel, State> {
        override fun create(
            viewModelContext: ViewModelContext, state: State
        ): SpotifyPlaylistViewModel = SpotifyPlaylistViewModel(
            state,
            viewModelContext.activity.get(),
            viewModelContext.app()
        )
    }
}

internal fun GetPlaylistTracks.intoState(
    state: State
) = this(args = GetPlaylistTracks.Args(state.playlist.id, state.tracks.offset), applySchedulers = false)
    .mapData { tracksPage -> tracksPage.map(::Track) }
