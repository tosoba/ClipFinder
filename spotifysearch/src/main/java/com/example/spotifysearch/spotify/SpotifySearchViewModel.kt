package com.example.spotifysearch.spotify

import android.arch.lifecycle.MutableLiveData
import com.example.coreandroid.base.vm.BaseViewModel
import com.example.coreandroid.mapper.spotify.ui
import com.example.spotifyapi.SpotifyApi
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.usecase.spotify.SearchSpotify
import javax.inject.Inject


class SpotifySearchViewModel @Inject constructor(
        private val searchSpotify: SearchSpotify
) : BaseViewModel() {

    val viewState: SpotifySearchViewState = SpotifySearchViewState()

    val loadedFlag: MutableLiveData<Unit> = MutableLiveData()

    private var currentOffset = 0
    private var totalItems = 0

    fun searchAll(query: String) {
        if (currentOffset == 0 || (currentOffset < totalItems)) {
            viewState.loadingInProgress.set(true)
            searchSpotify(SearchSpotify.Args(query, currentOffset))
                    .takeSuccessOnly()
                    .doFinally { viewState.loadingInProgress.set(false) }
                    .subscribeAndDisposeOnCleared({
                        currentOffset += SpotifyApi.DEFAULT_LIMIT
                        totalItems = it.totalItems
                        viewState.albums.addAll(it.albums.map(AlbumEntity::ui))
                        viewState.artists.addAll(it.artists.map(ArtistEntity::ui))
                        viewState.playlists.addAll(it.playlists.map(PlaylistEntity::ui))
                        viewState.tracks.addAll(it.tracks.map(TrackEntity::ui))
                        loadedFlag.value = Unit
                    }, ::onError)
        }
    }
}