package com.example.spotifyplaylist

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.model.mapData
import com.example.coreandroid.base.playlist.PlaylistViewState
import com.example.coreandroid.base.vm.MvRxViewModel
import com.example.coreandroid.mapper.spotify.domain
import com.example.coreandroid.mapper.spotify.ui
import com.example.coreandroid.model.Data
import com.example.coreandroid.model.LoadedSuccessfully
import com.example.coreandroid.model.Loading
import com.example.coreandroid.model.spotify.Playlist
import com.example.coreandroid.model.spotify.Track
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.usecase.spotify.DeleteSpotifyPlaylist
import com.example.there.domain.usecase.spotify.GetPlaylistTracks
import com.example.there.domain.usecase.spotify.InsertSpotifyPlaylist
import com.example.there.domain.usecase.spotify.IsSpotifyPlaylistSaved
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import timber.log.Timber

class PlaylistViewModel(
        initialState: PlaylistViewState<Playlist, Track>,
        private val getPlaylistTracks: GetPlaylistTracks,
        private val insertSpotifyPlaylist: InsertSpotifyPlaylist,
        private val deleteSpotifyPlaylist: DeleteSpotifyPlaylist,
        private val isSpotifyPlaylistSaved: IsSpotifyPlaylistSaved
) : MvRxViewModel<PlaylistViewState<Playlist, Track>>(initialState) {

    init {
        loadTracks()
        loadPlaylistFavouriteState()
    }

    fun loadTracks() {
        withState { state ->
            if (state.tracks.status is Loading) return@withState
            //TODO: check if offset works without current {}
            getPlaylistTracks(GetPlaylistTracks.Args(state.playlist.id, state.playlist.userId, state.tracks.offset), applySchedulers = false)
                    .mapData { tracksPage -> tracksPage.map(TrackEntity::ui) }
                    .subscribeOn(Schedulers.io())
                    .updateWithPagedResource(PlaylistViewState<Playlist, Track>::tracks) { copy(tracks = it) }
        }
    }

    fun togglePlaylistFavouriteState() = withState { state ->
        if (state.isSavedAsFavourite.value) deleteFavouritePlaylist(state.playlist)
        else addFavouritePlaylist(state.playlist)
    }

    private fun addFavouritePlaylist(playlist: Playlist) = insertSpotifyPlaylist(playlist.domain, applySchedulers = false)
            .subscribeOn(Schedulers.io())
            .subscribe({ setState { copy(isSavedAsFavourite = Data(true, LoadedSuccessfully)) } }, {
                setState { copy(isSavedAsFavourite = current { isSavedAsFavourite }.copyWithError(it)) }
                Timber.e(it)
            })
            .disposeOnClear()

    private fun deleteFavouritePlaylist(playlist: Playlist) = deleteSpotifyPlaylist(playlist.domain, applySchedulers = false)
            .subscribeOn(Schedulers.io())
            .subscribe({ setState { copy(isSavedAsFavourite = Data(false, LoadedSuccessfully)) } }, {
                setState { copy(isSavedAsFavourite = isSavedAsFavourite.copyWithError(it)) }
                Timber.e(it)
            })
            .disposeOnClear()

    private fun loadPlaylistFavouriteState() = withState { state ->
        if (state.isSavedAsFavourite.status is Loading) return@withState

        isSpotifyPlaylistSaved(state.playlist.id)
                .subscribeOn(Schedulers.io())
                .update(PlaylistViewState<Playlist, Track>::isSavedAsFavourite) { copy(isSavedAsFavourite = it) }
    }

    companion object : MvRxViewModelFactory<PlaylistViewModel, PlaylistViewState<Playlist, Track>> {
        override fun create(
                viewModelContext: ViewModelContext, state: PlaylistViewState<Playlist, Track>
        ): PlaylistViewModel {
            val getPlaylistTracks: GetPlaylistTracks by viewModelContext.activity.inject()
            val insertSpotifyPlaylist: InsertSpotifyPlaylist by viewModelContext.activity.inject()
            val deleteSpotifyPlaylist: DeleteSpotifyPlaylist by viewModelContext.activity.inject()
            val isSpotifyPlaylistSaved: IsSpotifyPlaylistSaved by viewModelContext.activity.inject()
            return PlaylistViewModel(state, getPlaylistTracks, insertSpotifyPlaylist, deleteSpotifyPlaylist, isSpotifyPlaylistSaved)
        }
    }
}