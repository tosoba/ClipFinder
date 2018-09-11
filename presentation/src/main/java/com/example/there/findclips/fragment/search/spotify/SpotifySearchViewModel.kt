package com.example.there.findclips.fragment.search.spotify

import android.arch.lifecycle.MutableLiveData
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.usecase.spotify.GetAccessToken
import com.example.there.domain.usecase.spotify.SearchSpotify
import com.example.there.findclips.base.vm.BaseSpotifyViewModel
import com.example.there.findclips.model.mapper.AlbumEntityMapper
import com.example.there.findclips.model.mapper.ArtistEntityMapper
import com.example.there.findclips.model.mapper.PlaylistEntityMapper
import com.example.there.findclips.model.mapper.TrackEntityMapper
import javax.inject.Inject

class SpotifySearchViewModel @Inject constructor(
        getAccessToken: GetAccessToken,
        private val searchSpotify: SearchSpotify
) : BaseSpotifyViewModel(getAccessToken) {

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
                    viewState.albums.addAll(it.albums.map(AlbumEntityMapper::mapFrom))
                    viewState.artists.addAll(it.artists.map(ArtistEntityMapper::mapFrom))
                    viewState.playlists.addAll(it.playlists.map(PlaylistEntityMapper::mapFrom))
                    viewState.tracks.addAll(it.tracks.map(TrackEntityMapper::mapFrom))
                    loadedFlag.value = Unit
                }, this::onError))
    }
}