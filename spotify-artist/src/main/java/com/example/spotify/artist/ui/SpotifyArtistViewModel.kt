package com.example.spotify.artist.ui

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.model.map
import com.example.core.model.mapData
import com.example.coreandroid.base.vm.MvRxViewModel
import com.example.coreandroid.mapper.spotify.domain
import com.example.coreandroid.mapper.spotify.ui
import com.example.coreandroid.model.*
import com.example.coreandroid.model.spotify.Artist
import com.example.coreandroid.util.ext.observeNetworkConnectivity
import com.example.spotify.artist.domain.usecase.*
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.entity.spotify.TrackEntity
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import timber.log.Timber

class SpotifyArtistViewModel(
    initialState: SpotifyArtistViewState,
    private val getAlbumsFromArtist: GetAlbumsFromArtist,
    private val getTopTracksFromArtist: GetTopTracksFromArtist,
    private val getRelatedArtists: GetRelatedArtists,
    private val insertArtist: InsertArtist,
    private val deleteArtist: DeleteArtist,
    private val isArtistSaved: IsArtistSaved,
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
        loadArtistFavouriteState()
    }

    fun loadAlbumsFromArtist(artistId: String, shouldClear: Boolean = false) = withState { state ->
        if (state.albums.status is Loading) return@withState

        val args = GetAlbumsFromArtist.Args(
            artistId = artistId,
            offset = if (shouldClear) 0 else state.albums.offset
        )
        getAlbumsFromArtist(args = args, applySchedulers = false)
            .mapData { albums -> albums.map(AlbumEntity::ui) }
            .subscribeOn(Schedulers.io())
            .updateWithPagedResource(SpotifyArtistViewState::albums, shouldClear = shouldClear) {
                copy(albums = it)
            }
    }

    fun loadTopTracksFromArtist(artistId: String) = withState { state ->
        if (state.topTracks.status is Loading) return@withState

        getTopTracksFromArtist(args = artistId, applySchedulers = false)
            .mapData { tracks -> tracks.map(TrackEntity::ui).sortedBy { it.name } }
            .subscribeOn(Schedulers.io())
            .updateWithResource(SpotifyArtistViewState::topTracks) { copy(topTracks = it) }
    }

    fun loadRelatedArtists(artistId: String) = withState { state ->
        if (state.relatedArtists.status is Loading) return@withState

        getRelatedArtists(args = artistId, applySchedulers = false)
            .mapData { artists -> artists.map(ArtistEntity::ui).sortedBy { it.name } }
            .subscribeOn(Schedulers.io())
            .updateWithResource(SpotifyArtistViewState::relatedArtists) { copy(relatedArtists = it) }
    }

    fun toggleArtistFavouriteState() = withState { state ->
        state.artists.value.lastOrNull()?.let {
            if (state.isSavedAsFavourite.value) deleteFavouriteArtist(it)
            else addFavouriteArtist(it)
        }
    }

    private fun addFavouriteArtist(artist: Artist) {
        insertArtist(artist.domain, applySchedulers = false)
            .subscribeOn(Schedulers.io())
            .subscribe({
                setState { copy(isSavedAsFavourite = Data(true, LoadedSuccessfully)) }
            }, {
                setState { copy(isSavedAsFavourite = isSavedAsFavourite.copyWithError(it)) }
                Timber.e(it)
            })
            .disposeOnClear()
    }

    private fun deleteFavouriteArtist(artist: Artist) {
        deleteArtist(artist.domain, applySchedulers = false)
            .subscribeOn(Schedulers.io())
            .subscribe({
                setState { copy(isSavedAsFavourite = Data(false, LoadedSuccessfully)) }
            }, {
                setState { copy(isSavedAsFavourite = isSavedAsFavourite.copyWithError(it)) }
                Timber.e(it)
            })
            .disposeOnClear()
    }

    private fun loadArtistFavouriteState() = withState { state ->
        if (state.isSavedAsFavourite.status is Loading) return@withState

        isArtistSaved(args = state.artists.value.last().id, applySchedulers = false)
            .subscribeOn(Schedulers.io())
            .update(SpotifyArtistViewState::isSavedAsFavourite) { copy(isSavedAsFavourite = it) }
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
        context.observeNetworkConnectivity {
            withState { (artists, albums, topTracks, relatedArtists) ->
                artists.value.lastOrNull()?.id?.let {
                    if (albums.isEmptyAndLastLoadingFailedWithNetworkError())
                        loadAlbumsFromArtist(it)
                    if (topTracks.isEmptyAndLastLoadingFailedWithNetworkError())
                        loadTopTracksFromArtist(it)
                    if (relatedArtists.isEmptyAndLastLoadingFailedWithNetworkError())
                        loadRelatedArtists(it)
                }
            }
        }.disposeOnClear()
    }

    companion object : MvRxViewModelFactory<SpotifyArtistViewModel, SpotifyArtistViewState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: SpotifyArtistViewState
        ): SpotifyArtistViewModel? {
            val getAlbumsFromArtist: GetAlbumsFromArtist by viewModelContext.activity.inject()
            val getTopTracksFromArtist: GetTopTracksFromArtist by viewModelContext.activity.inject()
            val getRelatedArtists: GetRelatedArtists by viewModelContext.activity.inject()
            val insertArtist: InsertArtist by viewModelContext.activity.inject()
            val deleteArtist: DeleteArtist by viewModelContext.activity.inject()
            val isArtistSaved: IsArtistSaved by viewModelContext.activity.inject()
            val spotifyPreferences: SpotifyPreferences by viewModelContext.activity.inject()
            return SpotifyArtistViewModel(
                state,
                getAlbumsFromArtist,
                getTopTracksFromArtist,
                getRelatedArtists,
                insertArtist,
                deleteArtist,
                isArtistSaved,
                spotifyPreferences,
                viewModelContext.app()
            )
        }
    }
}
