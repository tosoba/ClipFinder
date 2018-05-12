package com.example.there.findclips.playlist

import android.arch.lifecycle.MutableLiveData
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.usecases.spotify.GetAccessToken
import com.example.there.domain.usecases.spotify.GetPlaylistTracks
import com.example.there.findclips.base.BaseSpotifyViewModel
import com.example.there.findclips.entities.Playlist
import com.example.there.findclips.entities.Track
import com.example.there.findclips.mappers.TrackEntityMapper

class PlaylistViewModel(getAccessToken: GetAccessToken,
                        private val getPlaylistTracks: GetPlaylistTracks) : BaseSpotifyViewModel(getAccessToken) {

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

    private fun loadData(accessToken: AccessTokenEntity, playlist: Playlist) {
        viewState.loadingInProgress.set(true)
        addDisposable(getPlaylistTracks.execute(accessToken, playlist.id, playlist.userId)
                .doFinally { viewState.loadingInProgress.set(false) }
                .subscribe({
                    tracks.value = it.map(TrackEntityMapper::mapFrom).sortedBy { it.name }
                }, this::onError))
    }
}