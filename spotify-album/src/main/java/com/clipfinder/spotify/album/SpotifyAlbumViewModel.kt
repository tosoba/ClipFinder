package com.clipfinder.spotify.album

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.spotify.usecase.GetArtists
import com.clipfinder.core.spotify.usecase.GetTracksFromAlbum
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.model.PagedList
import com.example.core.android.spotify.model.Artist
import com.example.core.android.spotify.model.SimplifiedArtist
import com.example.core.android.spotify.model.Track
import com.example.core.android.util.ext.offset
import com.example.core.android.util.ext.retryLoadCollectionOnConnected
import com.example.core.ext.map
import com.example.core.ext.mapData
import com.example.core.model.Paged
import com.example.core.model.Resource
import io.reactivex.Single
import org.koin.android.ext.android.get

private typealias State = SpotifyAlbumViewState

class SpotifyAlbumViewModel(
    initialState: State,
    private val getArtists: GetArtists,
    private val getTracksFromAlbum: GetTracksFromAlbum,
    context: Context
) : MvRxViewModel<State>(initialState) {

    init {
        loadAlbumsArtists()
        loadTracksFromAlbum()
        handleConnectivityChanges(context)
    }

    fun loadAlbumsArtists() {
        loadCollection(State::artists, getArtists::intoState) { copy(artists = it) }
    }

    fun loadTracksFromAlbum() {
        loadPaged(State::tracks, getTracksFromAlbum::intoState, ::PagedList) {
            copy(tracks = it)
        }
    }

    fun clearArtistsError() = clearErrorIn(State::artists) { copy(artists = it) }
    fun clearTracksError() = clearErrorIn(State::tracks) { copy(tracks = it) }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context.handleConnectivityChanges { (_, artists, tracks) ->
            if (artists.retryLoadCollectionOnConnected) loadAlbumsArtists()
            if (tracks.retryLoadCollectionOnConnected) loadTracksFromAlbum()
        }
    }

    companion object : MvRxViewModelFactory<SpotifyAlbumViewModel, State> {
        override fun create(
            viewModelContext: ViewModelContext, state: State
        ): SpotifyAlbumViewModel = SpotifyAlbumViewModel(
            state,
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.app()
        )
    }
}

private fun GetArtists.intoState(
    state: State
): Single<Resource<List<Artist>>> = this(args = state.album.artists.map(SimplifiedArtist::id), applySchedulers = false)
    .mapData { artists -> artists.map(::Artist).sortedBy(Artist::name) }

private fun GetTracksFromAlbum.intoState(
    state: State
): Single<Resource<Paged<List<Track>>>> = this(args = GetTracksFromAlbum.Args(state.album.id, state.tracks.offset), applySchedulers = false)
    .mapData { tracksPage -> tracksPage.map(::Track) }
