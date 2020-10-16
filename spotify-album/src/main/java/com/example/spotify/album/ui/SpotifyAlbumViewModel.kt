package com.example.spotify.album.ui

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.spotify.usecase.GetArtists
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.model.retryLoadItemsOnNetworkAvailable
import com.example.core.android.spotify.model.Artist
import com.example.core.android.spotify.model.Track
import com.example.core.model.map
import com.example.core.model.mapData
import com.example.spotify.album.domain.usecase.GetTracksFromAlbum
import org.koin.android.ext.android.get

private typealias State = SpotifyAlbumViewState

class SpotifyAlbumViewModel(
    initialState: SpotifyAlbumViewState,
    private val getArtists: GetArtists,
    private val getTracksFromAlbum: GetTracksFromAlbum,
    context: Context
) : MvRxViewModel<SpotifyAlbumViewState>(initialState) {

    init {
        loadAlbumsArtists()
        loadTracksFromAlbum()
        handleConnectivityChanges(context)
    }

    fun loadAlbumsArtists() = load(State::artists, getArtists::intoState) { copy(artists = it) }

    fun loadTracksFromAlbum() = load(State::tracks, getTracksFromAlbum::intoState) { copy(tracks = it) }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context.handleConnectivityChanges { (_, artists, tracks) ->
            if (artists.retryLoadItemsOnNetworkAvailable)
                loadAlbumsArtists()
            if (tracks.retryLoadItemsOnNetworkAvailable)
                loadTracksFromAlbum()
        }
    }

    companion object : MvRxViewModelFactory<SpotifyAlbumViewModel, SpotifyAlbumViewState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: SpotifyAlbumViewState
        ): SpotifyAlbumViewModel = SpotifyAlbumViewModel(
            state,
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.app()
        )
    }
}

private fun GetArtists.intoState(
    state: SpotifyAlbumViewState
) = this(args = state.album.artists.map { it.id }, applySchedulers = false)
    .mapData { artists -> artists.map { Artist(it) }.sortedBy { it.name } }

private fun GetTracksFromAlbum.intoState(
    state: SpotifyAlbumViewState
) = this(args = GetTracksFromAlbum.Args(state.album.id, state.tracks.offset), applySchedulers = false)
    .mapData { tracksPage -> tracksPage.map { Track(it) } }
