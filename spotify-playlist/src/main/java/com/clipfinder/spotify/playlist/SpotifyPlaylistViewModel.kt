package com.clipfinder.spotify.playlist

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.android.base.playlist.PlaylistViewState
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.model.retryLoadItemsOnNetworkAvailable
import com.example.core.android.spotify.model.Playlist
import com.example.core.android.spotify.model.Track
import com.example.core.ext.map
import com.example.core.ext.mapData
import com.clipfinder.core.spotify.usecase.GetPlaylistTracks
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class SpotifyPlaylistViewModel(
    initialState: PlaylistViewState<Playlist, Track>,
    private val getPlaylistTracks: GetPlaylistTracks,
    context: Context
) : MvRxViewModel<PlaylistViewState<Playlist, Track>>(initialState) {

    init {
        loadTracks()
        handleConnectivityChanges(context)
    }

    fun loadTracks() = withState { (playlist, tracks) ->
        if (!tracks.shouldLoadMore) return@withState

        val args = GetPlaylistTracks.Args(playlist.id, tracks.offset)
        getPlaylistTracks(args = args, applySchedulers = false)
            .mapData { tracksPage -> tracksPage.map(::Track) }
            .subscribeOn(Schedulers.io())
            .updateWithPagedResource(PlaylistViewState<Playlist, Track>::tracks) { copy(tracks = it) }
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context.handleConnectivityChanges { (_, tracks) ->
            if (tracks.retryLoadItemsOnNetworkAvailable) loadTracks()
        }
    }

    companion object : MvRxViewModelFactory<SpotifyPlaylistViewModel, PlaylistViewState<Playlist, Track>> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: PlaylistViewState<Playlist, Track>
        ): SpotifyPlaylistViewModel {
            val getPlaylistTracks: GetPlaylistTracks by viewModelContext.activity.inject()
            return SpotifyPlaylistViewModel(
                state,
                getPlaylistTracks,
                viewModelContext.app()
            )
        }
    }
}
