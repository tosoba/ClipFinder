package com.example.spotify.artist.ui

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.model.DataList
import com.example.core.android.model.Loading
import com.example.core.android.model.shouldLoadOnNetworkAvailable
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Artist
import com.example.core.android.spotify.model.Track
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.android.util.ext.observeNetworkConnectivity
import com.example.core.model.map
import com.example.core.model.mapData
import com.example.spotify.artist.domain.usecase.GetAlbumsFromArtist
import com.example.spotify.artist.domain.usecase.GetRelatedArtists
import com.example.spotify.artist.domain.usecase.GetTopTracksFromArtist
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class SpotifyArtistViewModel(
    initialState: SpotifyArtistViewState,
    private val getAlbumsFromArtist: GetAlbumsFromArtist,
    private val getTopTracksFromArtist: GetTopTracksFromArtist,
    private val getRelatedArtists: GetRelatedArtists,
    private val preferences: SpotifyPreferences,
    context: Context
) : MvRxViewModel<SpotifyArtistViewState>(initialState) {

    init {
        loadData(initialState.artists.value.last())
        handlePreferencesChanges()
        handleConnectivityChanges(context)
    }

    fun onBackPressed() = withState { state ->
        if (state.artists.value.size < 2) {
            setState { copy(artists = DataList(emptyList())) }
            return@withState
        }

        val previousArtist = state.artists.value.elementAt(state.artists.value.size - 2)
        setState { copy(artists = DataList(artists.value.take(artists.value.size - 1))) }
        loadData(previousArtist, clearAlbums = true)
    }

    fun updateArtist(artist: Artist) {
        setState { copy(artists = artists.copyWithNewItems(artist)) }
        loadData(artist, clearAlbums = true)
    }

    private fun loadData(artist: Artist, clearAlbums: Boolean = false) {
        loadAlbumsFromArtist(artist.id, shouldClear = clearAlbums)
        loadTopTracksFromArtist(artist.id)
        loadRelatedArtists(artist.id)
    }

    fun loadAlbumsFromArtist(artistId: String, shouldClear: Boolean = false) = withState { state ->
        if (!state.albums.shouldLoad) return@withState

        val args = GetAlbumsFromArtist.Args(
            artistId = artistId,
            offset = if (shouldClear) 0 else state.albums.offset
        )
        getAlbumsFromArtist(args = args, applySchedulers = false)
            .mapData { albums -> albums.map { Album(it) } }
            .subscribeOn(Schedulers.io())
            .updateWithPagedResource(SpotifyArtistViewState::albums, shouldClear = shouldClear) {
                copy(albums = it)
            }
    }

    fun loadTopTracksFromArtist(artistId: String) = withState { state ->
        if (state.topTracks.status is Loading) return@withState

        getTopTracksFromArtist(args = artistId, applySchedulers = false)
            .mapData { tracks -> tracks.map { Track(it) }.sortedBy { it.name } }
            .subscribeOn(Schedulers.io())
            .updateWithResource(SpotifyArtistViewState::topTracks) { copy(topTracks = it) }
    }

    fun loadRelatedArtists(artistId: String) = withState { state ->
        if (state.relatedArtists.status is Loading) return@withState

        getRelatedArtists(args = artistId, applySchedulers = false)
            .mapData { artists -> artists.map { Artist(it) }.sortedBy { it.name } }
            .subscribeOn(Schedulers.io())
            .updateWithResource(SpotifyArtistViewState::relatedArtists) {
                copy(relatedArtists = it)
            }
    }

    private fun handlePreferencesChanges() {
        preferences.countryObservable
            .skip(1)
            .distinctUntilChanged()
            .subscribe {
                withState { (artists) ->
                    artists.value.lastOrNull()?.let { loadAlbumsFromArtist(it.id) }
                }
            }
            .disposeOnClear()
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context
            .observeNetworkConnectivity {
                withState { (artists, albums, topTracks, relatedArtists) ->
                    artists.value.lastOrNull()?.id?.let {
                        if (albums.shouldLoadOnNetworkAvailable())
                            loadAlbumsFromArtist(it)
                        if (topTracks.shouldLoadOnNetworkAvailable())
                            loadTopTracksFromArtist(it)
                        if (relatedArtists.shouldLoadOnNetworkAvailable())
                            loadRelatedArtists(it)
                    }
                }
            }
            .disposeOnClear()
    }

    companion object : MvRxViewModelFactory<SpotifyArtistViewModel, SpotifyArtistViewState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: SpotifyArtistViewState
        ): SpotifyArtistViewModel? {
            val getAlbumsFromArtist: GetAlbumsFromArtist by viewModelContext.activity.inject()
            val getTopTracksFromArtist: GetTopTracksFromArtist by viewModelContext.activity.inject()
            val getRelatedArtists: GetRelatedArtists by viewModelContext.activity.inject()
            val spotifyPreferences: SpotifyPreferences by viewModelContext.activity.inject()
            return SpotifyArtistViewModel(
                state,
                getAlbumsFromArtist,
                getTopTracksFromArtist,
                getRelatedArtists,
                spotifyPreferences,
                viewModelContext.app()
            )
        }
    }
}
