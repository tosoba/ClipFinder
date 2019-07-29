package com.example.spotifyplaylist

import android.util.Log
import com.example.coreandroid.base.playlist.BasePlaylistViewModel
import com.example.coreandroid.mapper.spotify.domain
import com.example.coreandroid.mapper.spotify.ui
import com.example.coreandroid.model.spotify.Playlist
import com.example.coreandroid.model.spotify.Track
import com.example.spotifyapi.SpotifyApi
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.usecase.spotify.DeleteSpotifyPlaylist
import com.example.there.domain.usecase.spotify.GetPlaylistTracks
import com.example.there.domain.usecase.spotify.InsertSpotifyPlaylist
import com.example.there.domain.usecase.spotify.IsSpotifyPlaylistSaved

class PlaylistViewModel(
        private val getPlaylistTracks: GetPlaylistTracks,
        private val insertSpotifyPlaylist: InsertSpotifyPlaylist,
        private val deleteSpotifyPlaylist: DeleteSpotifyPlaylist,
        private val isSpotifyPlaylistSaved: IsSpotifyPlaylistSaved
) : BasePlaylistViewModel<Track>() {

    fun loadTracks(playlist: Playlist) {
        loadData(playlist)
        loadPlaylistFavouriteState(playlist)
    }

    private var currentOffset = 0
    private var totalItems = 0

    private fun loadData(playlist: Playlist) {
        if (currentOffset == 0 || (currentOffset < totalItems)) {
            viewState.loadingInProgress.set(true)
            getPlaylistTracks(GetPlaylistTracks.Args(playlist.id, playlist.userId, currentOffset))
                    .takeSuccessOnly()
                    .doFinally { viewState.loadingInProgress.set(false) }
                    .subscribeAndDisposeOnCleared({
                        currentOffset = it.offset + SpotifyApi.DEFAULT_TRACKS_LIMIT
                        totalItems = it.totalItems
                        mutableTracks.value = it.items.map(TrackEntity::ui)
                    }, ::onError)
        }
    }

    fun addFavouritePlaylist(
            playlist: Playlist
    ) = insertSpotifyPlaylist(playlist.domain)
            .subscribeAndDisposeOnCleared({ viewState.isSavedAsFavourite.set(true) }, { Log.e(javaClass.name, "Insert error.") })

    fun deleteFavouritePlaylist(
            playlist: Playlist
    ) = deleteSpotifyPlaylist(playlist.domain)
            .subscribeAndDisposeOnCleared({ viewState.isSavedAsFavourite.set(false) }, { Log.e(javaClass.name, "Delete error.") })

    private fun loadPlaylistFavouriteState(
            playlist: Playlist
    ) = isSpotifyPlaylistSaved(playlist.id)
            .subscribeAndDisposeOnCleared(viewState.isSavedAsFavourite::set)
}