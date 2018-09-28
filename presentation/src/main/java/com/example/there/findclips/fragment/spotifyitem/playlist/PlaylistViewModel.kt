package com.example.there.findclips.fragment.spotifyitem.playlist

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.there.data.api.spotify.SpotifyApi
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.usecase.spotify.*
import com.example.there.findclips.base.vm.BaseSpotifyViewModel
import com.example.there.findclips.model.entity.Playlist
import com.example.there.findclips.model.entity.Track
import com.example.there.findclips.model.mapper.PlaylistEntityMapper
import com.example.there.findclips.model.mapper.TrackEntityMapper
import javax.inject.Inject

class PlaylistViewModel @Inject constructor(
        getAccessToken: GetAccessToken,
        private val getPlaylistTracks: GetPlaylistTracks,
        private val insertSpotifyPlaylist: InsertSpotifyPlaylist,
        private val deletePlaylist: DeletePlaylist,
        private val isPlaylistSaved: IsPlaylistSaved
) : BaseSpotifyViewModel(getAccessToken) {

    val viewState: PlaylistViewState = PlaylistViewState()

    val tracks: MutableLiveData<List<Track>> = MutableLiveData()

    fun loadTracks(accessToken: AccessTokenEntity?, playlist: Playlist) {
        if (accessToken != null && accessToken.isValid) {
            accessTokenLiveData.value = accessToken
            loadData(accessToken, playlist)
            loadPlaylistFavouriteState(playlist)
        } else {
            loadAccessToken {
                loadData(it, playlist)
                loadPlaylistFavouriteState(playlist)
            }
        }
    }

    private var currentOffset = 0
    private var totalItems = 0

    private fun loadData(accessToken: AccessTokenEntity, playlist: Playlist) {
        if (currentOffset == 0 || (currentOffset < totalItems)) {
            viewState.loadingInProgress.set(true)
            addDisposable(getPlaylistTracks.execute(accessToken, playlist.id, playlist.userId, currentOffset)
                    .doFinally { viewState.loadingInProgress.set(false) }
                    .subscribe({
                        currentOffset = it.offset + SpotifyApi.DEFAULT_TRACKS_LIMIT
                        totalItems = it.totalItems
                        tracks.value = it.items.map(TrackEntityMapper::mapFrom)
                    }, ::onError))
        }
    }

    fun addFavouritePlaylist(
            playlist: Playlist
    ) = addDisposable(insertSpotifyPlaylist.execute(PlaylistEntityMapper.mapBack(playlist))
            .subscribe({ viewState.isSavedAsFavourite.set(true) }, { Log.e(javaClass.name, "Insert error.") }))

    fun deleteFavouritePlaylist(
            playlist: Playlist
    ) = addDisposable(deletePlaylist.execute(PlaylistEntityMapper.mapBack(playlist))
            .subscribe({ viewState.isSavedAsFavourite.set(false) }, { Log.e(javaClass.name, "Delete error.") }))

    private fun loadPlaylistFavouriteState(
            playlist: Playlist
    ) = addDisposable(isPlaylistSaved.execute(PlaylistEntityMapper.mapBack(playlist))
            .subscribe({ viewState.isSavedAsFavourite.set(it) }, {}))
}