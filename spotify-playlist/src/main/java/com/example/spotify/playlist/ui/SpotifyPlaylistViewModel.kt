package com.example.spotify.playlist.ui

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.android.base.playlist.PlaylistViewState
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.mapper.spotify.ui
import com.example.core.android.model.Loading
import com.example.core.android.model.isEmptyAndLastLoadingFailedWithNetworkError
import com.example.core.android.spotify.model.Track
import com.example.core.android.spotify.model.Playlist
import com.example.core.android.util.ext.observeNetworkConnectivity
import com.example.core.model.map
import com.example.core.model.mapData
import com.example.spotify.playlist.domain.usecase.GetPlaylistTracks
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.usecase.spotify.IsSpotifyPlaylistSaved
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class SpotifyPlaylistViewModel(
    initialState: PlaylistViewState<Playlist, Track>,
    private val getPlaylistTracks: GetPlaylistTracks,
    private val isSpotifyPlaylistSaved: IsSpotifyPlaylistSaved,
    context: Context
) : MvRxViewModel<PlaylistViewState<Playlist, Track>>(initialState) {

    init {
        loadTracks()
        loadPlaylistFavouriteState()
        handleConnectivityChanges(context)
    }

    fun loadTracks() = withState { (playlist, tracks) ->
        if (!tracks.shouldLoad) return@withState

        val args = GetPlaylistTracks.Args(playlist.id, tracks.offset)
        getPlaylistTracks(args = args, applySchedulers = false)
            .mapData { tracksPage -> tracksPage.map(TrackEntity::ui) }
            .subscribeOn(Schedulers.io())
            .updateWithPagedResource(PlaylistViewState<Playlist, Track>::tracks) { copy(tracks = it) }
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
            val isSpotifyPlaylistSaved: IsSpotifyPlaylistSaved by viewModelContext.activity.inject()
            return SpotifyPlaylistViewModel(
                state,
                getPlaylistTracks,
                isSpotifyPlaylistSaved,
                viewModelContext.app()
            )
        }
    }
}
