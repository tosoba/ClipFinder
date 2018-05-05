package com.example.there.findclips.search.spotify

import android.arch.lifecycle.MutableLiveData
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.usecases.spotify.AccessTokenUseCase
import com.example.there.domain.usecases.spotify.SearchAllUseCase
import com.example.there.findclips.base.BaseSpotifyViewModel
import com.example.there.findclips.mappers.AlbumEntityMapper
import com.example.there.findclips.mappers.ArtistEntityMapper
import com.example.there.findclips.mappers.PlaylistEntityMapper
import com.example.there.findclips.mappers.TrackEntityMapper

class SpotifySearchViewModel(accessTokenUseCase: AccessTokenUseCase,
                             private val searchAllUseCase: SearchAllUseCase) : BaseSpotifyViewModel(accessTokenUseCase) {

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
        addDisposable(searchAllUseCase.searchAll(accessTokenEntity, query)
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