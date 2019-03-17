package com.example.there.findclips.spotify.spotifyitem.playlist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.usecase.spotify.DeleteSpotifyPlaylist
import com.example.there.domain.usecase.spotify.GetPlaylistTracks
import com.example.there.domain.usecase.spotify.InsertSpotifyPlaylist
import com.example.there.domain.usecase.spotify.IsSpotifyPlaylistSaved
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.model.entity.spotify.Playlist
import com.example.there.findclips.model.entity.spotify.Track
import com.example.there.findclips.model.mapper.spotify.domain
import com.example.there.findclips.model.mapper.spotify.ui
import javax.inject.Inject

class PlaylistViewModel @Inject constructor(
        private val getPlaylistTracks: GetPlaylistTracks,
        private val insertSpotifyPlaylist: InsertSpotifyPlaylist,
        private val deleteSpotifyPlaylist: DeleteSpotifyPlaylist,
        private val isSpotifyPlaylistSaved: IsSpotifyPlaylistSaved
) : BaseViewModel() {

    val viewState: PlaylistViewState = PlaylistViewState()

    private val mutableTracks: MutableLiveData<List<Track>> = MutableLiveData()
    val tracks: LiveData<List<Track>>
        get() = mutableTracks

    fun loadTracks(playlist: Playlist) {
        loadData(playlist)
        loadPlaylistFavouriteState(playlist)
    }

    private var currentOffset = 0
    private var totalItems = 0

    private fun loadData(playlist: Playlist) {
        if (currentOffset == 0 || (currentOffset < totalItems)) {
            viewState.loadingInProgress.set(true)
            getPlaylistTracks.execute(GetPlaylistTracks.Input(playlist.id, playlist.userId, currentOffset))
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
    ) = insertSpotifyPlaylist.execute(playlist.domain)
            .subscribeAndDisposeOnCleared({ viewState.isSavedAsFavourite.set(true) }, { Log.e(javaClass.name, "Insert error.") })

    fun deleteFavouritePlaylist(
            playlist: Playlist
    ) = deleteSpotifyPlaylist.execute(playlist.domain)
            .subscribeAndDisposeOnCleared({ viewState.isSavedAsFavourite.set(false) }, { Log.e(javaClass.name, "Delete error.") })

    private fun loadPlaylistFavouriteState(
            playlist: Playlist
    ) = isSpotifyPlaylistSaved.execute(playlist.domain)
            .subscribeAndDisposeOnCleared(viewState.isSavedAsFavourite::set)
}