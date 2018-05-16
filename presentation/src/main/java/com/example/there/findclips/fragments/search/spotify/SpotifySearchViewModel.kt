package com.example.there.findclips.fragments.search.spotify

import android.arch.lifecycle.MutableLiveData
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.usecases.spotify.GetAccessToken
import com.example.there.domain.usecases.spotify.SearchSpotify
import com.example.there.findclips.base.BaseSpotifyViewModel
import com.example.there.findclips.model.mappers.AlbumEntityMapper
import com.example.there.findclips.model.mappers.ArtistEntityMapper
import com.example.there.findclips.model.mappers.PlaylistEntityMapper
import com.example.there.findclips.model.mappers.TrackEntityMapper

class SpotifySearchViewModel(getAccessToken: GetAccessToken,
                             private val searchSpotify: SearchSpotify) : BaseSpotifyViewModel(getAccessToken) {

    val viewState: SpotifySearchViewState = SpotifySearchViewState()

    val loadedFlag: MutableLiveData<Unit> = MutableLiveData()

    fun searchAll(accessToken: AccessTokenEntity?, query: String) {
        if (accessToken != null && accessToken.isValid) {
            accessTokenLiveData.value = accessToken
            loadData(accessToken, query)
        } else {
            loadAccessToken { loadData(it, query) }
        }
    }

    private fun loadData(accessTokenEntity: AccessTokenEntity, query: String) {
        viewState.loadingInProgress.set(true)
        addDisposable(searchSpotify.execute(accessTokenEntity, query)
                .doFinally { viewState.loadingInProgress.set(false) }
                .subscribe({
                    viewState.addAlbumsSorted(it.albums.map(AlbumEntityMapper::mapFrom))
                    viewState.addArtistsSorted(it.artists.map(ArtistEntityMapper::mapFrom))
                    viewState.addPlaylistsSorted(it.playlists.map(PlaylistEntityMapper::mapFrom))
                    viewState.addTracksSorted(it.tracks.map(TrackEntityMapper::mapFrom))
                    loadedFlag.value = Unit
                }, this::onError))
    }
}