package com.example.there.findclips.activities.playlist

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.there.data.apis.spotify.SpotifyApi
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.usecases.spotify.GetAccessToken
import com.example.there.domain.usecases.spotify.GetPlaylistTracks
import com.example.there.domain.usecases.spotify.InsertSpotifyPlaylist
import com.example.there.findclips.base.vm.BaseSpotifyViewModel
import com.example.there.findclips.model.entities.Playlist
import com.example.there.findclips.model.entities.Track
import com.example.there.findclips.model.mappers.PlaylistEntityMapper
import com.example.there.findclips.model.mappers.TrackEntityMapper
import javax.inject.Inject

class PlaylistViewModel @Inject constructor(
        getAccessToken: GetAccessToken,
        private val getPlaylistTracks: GetPlaylistTracks,
        private val insertSpotifyPlaylist: InsertSpotifyPlaylist
) : BaseSpotifyViewModel(getAccessToken) {

    val viewState: PlaylistViewState = PlaylistViewState()

    val tracks: MutableLiveData<List<Track>> = MutableLiveData()

    fun loadTracks(accessToken: AccessTokenEntity?, playlist: Playlist) {
        if (accessToken != null && accessToken.isValid) {
            accessTokenLiveData.value = accessToken
            loadData(accessToken, playlist)
        } else {
            loadAccessToken { loadData(it, playlist) }
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
                        currentOffset = it.offset + SpotifyApi.DEFAULT_TRACKS_LIMIT.toInt()
                        totalItems = it.totalItems
                        tracks.value = it.tracks.map(TrackEntityMapper::mapFrom)
                    }, ::onError))
        }
    }

    fun addFavouritePlaylist(playlist: Playlist) {
        addDisposable(insertSpotifyPlaylist.execute(PlaylistEntityMapper.mapBack(playlist)).subscribe({}, { Log.e(javaClass.name, "Insert error.") }))
    }
}