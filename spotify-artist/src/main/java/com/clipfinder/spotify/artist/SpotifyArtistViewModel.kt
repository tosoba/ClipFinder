package com.clipfinder.spotify.artist

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.model.DataList
import com.example.core.android.model.Loading
import com.example.core.android.model.retryLoadItemsOnNetworkAvailable
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Artist
import com.example.core.android.spotify.model.Track
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.model.map
import com.example.core.model.mapData
import com.clipfinder.core.spotify.usecase.GetAlbumsFromArtist
import com.clipfinder.core.spotify.usecase.GetRelatedArtists
import com.clipfinder.core.spotify.usecase.GetTopTracksFromArtist
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.get

private typealias State = SpotifyArtistViewState

class SpotifyArtistViewModel(
    initialState: State,
    private val getAlbumsFromArtist: GetAlbumsFromArtist,
    private val getTopTracksFromArtist: GetTopTracksFromArtist,
    private val getRelatedArtists: GetRelatedArtists,
    private val preferences: SpotifyPreferences,
    context: Context
) : MvRxViewModel<State>(initialState) {

    init {
        loadData()
        handlePreferencesChanges()
        handleConnectivityChanges(context)
    }

    fun onBackPressed() = withState { state ->
        if (state.artists.value.size < 2) {
            setState { copy(artists = DataList(emptyList())) }
            return@withState
        }

        setState { copy(artists = DataList(artists.value.take(artists.value.size - 1))) }
        loadData(clearAlbums = true)
    }

    fun updateArtist(artist: Artist) {
        setState { copy(artists = artists.copyWithNewItems(artist)) }
        loadData(clearAlbums = true)
    }

    private fun loadData(clearAlbums: Boolean = false) {
        loadAlbumsFromArtist(shouldClear = clearAlbums)
        loadTopTracksFromArtist()
        loadRelatedArtists()
    }

    fun loadAlbumsFromArtist(shouldClear: Boolean = false) = withState { state ->
        if (!state.albums.shouldLoadMore) return@withState

        val args = GetAlbumsFromArtist.Args(
            artistId = state.artists.value.last().id,
            offset = if (shouldClear) 0 else state.albums.offset
        )
        getAlbumsFromArtist(args = args, applySchedulers = false)
            .mapData { albums -> albums.map(::Album) }
            .subscribeOn(Schedulers.io())
            .updateWithPagedResource(State::albums, shouldClear = shouldClear) { copy(albums = it) }
    }

    fun loadTopTracksFromArtist() = withState { state ->
        if (state.topTracks.status is Loading) return@withState

        getTopTracksFromArtist(args = state.artists.value.last().id, applySchedulers = false)
            .mapData { tracks -> tracks.map(::Track).sortedBy(Track::name) }
            .subscribeOn(Schedulers.io())
            .updateWithResource(State::topTracks) { copy(topTracks = it) }
    }

    fun loadRelatedArtists() = withState { state ->
        if (state.relatedArtists.status is Loading) return@withState

        getRelatedArtists(args = state.artists.value.last().id, applySchedulers = false)
            .mapData { artists -> artists.map(::Artist).sortedBy(Artist::name) }
            .subscribeOn(Schedulers.io())
            .updateWithResource(State::relatedArtists) { copy(relatedArtists = it) }
    }

    private fun handlePreferencesChanges() {
        preferences.countryObservable
            .skip(1)
            .distinctUntilChanged()
            .subscribe { loadAlbumsFromArtist() }
            .disposeOnClear()
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context.handleConnectivityChanges { (_, albums, topTracks, relatedArtists) ->
            if (albums.retryLoadItemsOnNetworkAvailable) loadAlbumsFromArtist()
            if (topTracks.retryLoadItemsOnNetworkAvailable) loadTopTracksFromArtist()
            if (relatedArtists.retryLoadItemsOnNetworkAvailable) loadRelatedArtists()
        }
    }

    companion object : MvRxViewModelFactory<SpotifyArtistViewModel, State> {
        override fun create(
            viewModelContext: ViewModelContext, state: State
        ): SpotifyArtistViewModel = SpotifyArtistViewModel(
            state,
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.app()
        )
    }
}
