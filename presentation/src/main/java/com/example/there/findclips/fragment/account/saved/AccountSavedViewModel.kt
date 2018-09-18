package com.example.there.findclips.fragment.account.saved

import com.example.there.data.api.spotify.SpotifyApi
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.usecase.spotify.GetCurrentUsersSavedAlbums
import com.example.there.domain.usecase.spotify.GetCurrentUsersSavedTracks
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.model.mapper.AlbumEntityMapper
import com.example.there.findclips.model.mapper.TrackEntityMapper
import javax.inject.Inject

class AccountSavedViewModel @Inject constructor(
        private val getCurrentUsersSavedTracks: GetCurrentUsersSavedTracks,
        private val getCurrentUsersSavedAlbums: GetCurrentUsersSavedAlbums
) : BaseViewModel() {

    lateinit var viewState: AccountSavedViewState

    private var currentTracksOffset = 0
    private var totalTracks = 0

    fun loadTracks(accessToken: AccessTokenEntity) {
        if (canLoadTracks) {
            viewState.tracksLoadingInProgress.set(true)
            addDisposable(getCurrentUsersSavedTracks.execute(accessToken, currentTracksOffset)
                    .doFinally { viewState.tracksLoadingInProgress.set(false) }
                    .subscribe({
                        viewState.tracks.addAll(it.items.map(TrackEntityMapper::mapFrom))
                        currentTracksOffset = it.offset + SpotifyApi.DEFAULT_LIMIT.toInt()
                        totalTracks = it.totalItems
                    }, ::onError))
        }
    }

    private val canLoadTracks: Boolean
        get() = viewState.tracksLoadingInProgress.get() == false &&
                viewState.userLoggedIn.get() == true &&
                (currentTracksOffset == 0 || (currentTracksOffset < totalTracks))

    private var currentAlbumsOffset = 0
    private var totalAlbums = 0

    fun loadAlbums(accessToken: AccessTokenEntity) {
        if (canLoadAlbums) {
            viewState.albumsLoadingInProgress.set(true)
            addDisposable(getCurrentUsersSavedAlbums.execute(accessToken, currentAlbumsOffset)
                    .doFinally { viewState.albumsLoadingInProgress.set(false) }
                    .subscribe({
                        viewState.albums.addAll(it.items.map(AlbumEntityMapper::mapFrom))
                        currentAlbumsOffset = it.offset + SpotifyApi.DEFAULT_LIMIT.toInt()
                        totalAlbums = it.totalItems
                    }, ::onError))
        }
    }

    private val canLoadAlbums: Boolean
        get() = viewState.albumsLoadingInProgress.get() == false &&
                viewState.userLoggedIn.get() == true &&
                (currentAlbumsOffset == 0 || (currentAlbumsOffset < totalAlbums))
}