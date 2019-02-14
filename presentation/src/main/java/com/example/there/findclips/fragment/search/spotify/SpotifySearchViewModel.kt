package com.example.there.findclips.fragment.search.spotify

import android.arch.lifecycle.MutableLiveData
import com.example.there.data.api.spotify.SpotifyApi
import com.example.there.domain.usecase.spotify.SearchSpotify
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.model.mapper.AlbumEntityMapper
import com.example.there.findclips.model.mapper.ArtistEntityMapper
import com.example.there.findclips.model.mapper.PlaylistEntityMapper
import com.example.there.findclips.model.mapper.TrackEntityMapper
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
            addDisposable(searchSpotify.execute(SearchSpotify.Input(query, currentOffset))
                    .doFinally { viewState.loadingInProgress.set(false) }
                    .subscribe({
                        currentOffset += SpotifyApi.DEFAULT_LIMIT
                        totalItems = it.totalItems
                        viewState.albums.addAll(it.albums.map(AlbumEntityMapper::mapFrom))
                        viewState.artists.addAll(it.artists.map(ArtistEntityMapper::mapFrom))
                        viewState.playlists.addAll(it.playlists.map(PlaylistEntityMapper::mapFrom))
                        viewState.tracks.addAll(it.tracks.map(TrackEntityMapper::mapFrom))
                        loadedFlag.value = Unit
                    }, ::onError))
        }
    }
}