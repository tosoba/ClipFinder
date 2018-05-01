package com.example.there.findclips.search.spotify

import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.usecase.spotify.AccessTokenUseCase
import com.example.there.domain.usecase.spotify.SearchAllUseCase
import com.example.there.findclips.base.BaseSpotifyViewModel

class SpotifySearchViewModel(accessTokenUseCase: AccessTokenUseCase,
                             private val searchAllUseCase: SearchAllUseCase) : BaseSpotifyViewModel(accessTokenUseCase) {

    val viewState: SpotifySearchViewState = SpotifySearchViewState()

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
                    viewState.addAlbumsSorted(it.albums)
                    viewState.addArtistsSorted(it.artists)
                    viewState.addPlaylistsSorted(it.playlists)
                    viewState.addTracksSorted(it.tracks)
                }, this::onError))
    }
}