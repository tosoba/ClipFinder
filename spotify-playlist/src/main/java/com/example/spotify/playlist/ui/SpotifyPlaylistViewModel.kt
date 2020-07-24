package com.example.spotify.playlist.ui

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.model.map
import com.example.core.model.mapData
import com.example.coreandroid.base.playlist.PlaylistViewState
import com.example.coreandroid.base.vm.MvRxViewModel
import com.example.coreandroid.mapper.spotify.domain
import com.example.coreandroid.mapper.spotify.ui
import com.example.coreandroid.model.Data
import com.example.coreandroid.model.LoadedSuccessfully
import com.example.coreandroid.model.Loading
import com.example.coreandroid.model.isEmptyAndLastLoadingFailedWithNetworkError
import com.example.coreandroid.model.spotify.Playlist
import com.example.coreandroid.model.spotify.Track
import com.example.coreandroid.util.ext.observeNetworkConnectivity
import com.example.spotify.playlist.domain.usecase.GetPlaylistTracks
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.usecase.spotify.DeleteSpotifyPlaylist
import com.example.there.domain.usecase.spotify.InsertSpotifyPlaylist
import com.example.there.domain.usecase.spotify.IsSpotifyPlaylistSaved
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import timber.log.Timber

class SpotifyPlaylistViewModel(
    initialState: PlaylistViewState<Playlist, Track>,
    private val getPlaylistTracks: GetPlaylistTracks,
    private val insertSpotifyPlaylist: InsertSpotifyPlaylist,
    private val deleteSpotifyPlaylist: DeleteSpotifyPlaylist,
    private val isSpotifyPlaylistSaved: IsSpotifyPlaylistSaved,
    context: Context
) : MvRxViewModel<PlaylistViewState<Playlist, Track>>(initialState) {

    init {
        loadTracks()
        loadPlaylistFavouriteState()
        handleConnectivityChanges(context)
    }

    fun loadTracks() = withState { (playlist, tracks) ->
        if (tracks.status is Loading) return@withState
        val args = GetPlaylistTracks.Args(playlist.id, playlist.userId, tracks.offset)
        getPlaylistTracks(args = args, applySchedulers = false)
            .mapData { tracksPage -> tracksPage.map(TrackEntity::ui) }
            .subscribeOn(Schedulers.io())
            .updateWithPagedResource(PlaylistViewState<Playlist, Track>::tracks) { copy(tracks = it) }
    }

    fun togglePlaylistFavouriteState() = withState { (playlist, _, isSavedAsFavourite) ->
        if (isSavedAsFavourite.value) deleteFavouritePlaylist(playlist)
        else addFavouritePlaylist(playlist)
    }

    private fun addFavouritePlaylist(playlist: Playlist) {
        insertSpotifyPlaylist(playlist.domain, applySchedulers = false)
            .subscribeOn(Schedulers.io())
            .subscribe({
                setState { copy(isSavedAsFavourite = Data(true, LoadedSuccessfully)) }
            }, {
                setState { copy(isSavedAsFavourite = isSavedAsFavourite.copyWithError(it)) }
                Timber.e(it)
            })
            .disposeOnClear()
    }

    private fun deleteFavouritePlaylist(playlist: Playlist) {
        deleteSpotifyPlaylist(playlist.domain, applySchedulers = false)
            .subscribeOn(Schedulers.io())
            .subscribe({
                setState { copy(isSavedAsFavourite = Data(false, LoadedSuccessfully)) }
            }, {
                setState { copy(isSavedAsFavourite = isSavedAsFavourite.copyWithError(it)) }
                Timber.e(it)
            })
            .disposeOnClear()
    }

    private fun loadPlaylistFavouriteState() = withState { (playlist, _, isSavedAsFavourite) ->
        if (isSavedAsFavourite.status is Loading) return@withState
        isSpotifyPlaylistSaved(playlist.id)
            .subscribeOn(Schedulers.io())
            .update(PlaylistViewState<Playlist, Track>::isSavedAsFavourite) {
                copy(isSavedAsFavourite = it)
            }
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context.observeNetworkConnectivity {
            withState { (_, tracks) ->
                if (tracks.isEmptyAndLastLoadingFailedWithNetworkError()) loadTracks()
            }
        }.disposeOnClear()
    }

    companion object : MvRxViewModelFactory<SpotifyPlaylistViewModel, PlaylistViewState<Playlist, Track>> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: PlaylistViewState<Playlist, Track>
        ): SpotifyPlaylistViewModel {
            val getPlaylistTracks: GetPlaylistTracks by viewModelContext.activity.inject()
            val insertSpotifyPlaylist: InsertSpotifyPlaylist by viewModelContext.activity.inject()
            val deleteSpotifyPlaylist: DeleteSpotifyPlaylist by viewModelContext.activity.inject()
            val isSpotifyPlaylistSaved: IsSpotifyPlaylistSaved by viewModelContext.activity.inject()
            return SpotifyPlaylistViewModel(
                state,
                getPlaylistTracks,
                insertSpotifyPlaylist,
                deleteSpotifyPlaylist,
                isSpotifyPlaylistSaved,
                viewModelContext.app()
            )
        }
    }
}
