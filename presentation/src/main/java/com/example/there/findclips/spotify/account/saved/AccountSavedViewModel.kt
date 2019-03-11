package com.example.there.findclips.spotify.account.saved

import com.example.there.data.api.spotify.SpotifyApi
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.usecase.spotify.GetCurrentUsersSavedAlbums
import com.example.there.domain.usecase.spotify.GetCurrentUsersSavedTracks
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.model.mapper.ui
import javax.inject.Inject

class AccountSavedViewModel @Inject constructor(
        private val getCurrentUsersSavedTracks: GetCurrentUsersSavedTracks,
        private val getCurrentUsersSavedAlbums: GetCurrentUsersSavedAlbums
) : BaseViewModel() {

    lateinit var viewState: AccountSavedViewState

    private val canLoadTracks: Boolean
        get() = viewState.tracksLoadingInProgress.get() == false &&
                viewState.userLoggedIn.get() == true &&
                (currentTracksOffset == 0 || (currentTracksOffset < totalTracks))

    private var currentTracksOffset = 0
    private var totalTracks = 0

    fun loadTracks() {
        if (canLoadTracks) {
            viewState.tracksLoadingInProgress.set(true)
            getCurrentUsersSavedTracks.execute(currentTracksOffset)
                    .doFinally { viewState.tracksLoadingInProgress.set(false) }
                    .subscribeAndDisposeOnCleared({
                        viewState.tracks.addAll(it.items.map(TrackEntity::ui))
                        currentTracksOffset = it.offset + SpotifyApi.DEFAULT_LIMIT
                        totalTracks = it.totalItems
                        viewState.tracksLoadingErrorOccurred.set(false)
                    }, getOnErrorWith {
                        viewState.tracksLoadingErrorOccurred.set(true)
                    })
        }
    }

    private val canLoadAlbums: Boolean
        get() = viewState.albumsLoadingInProgress.get() == false &&
                viewState.userLoggedIn.get() == true &&
                (currentAlbumsOffset == 0 || (currentAlbumsOffset < totalAlbums))

    private var currentAlbumsOffset = 0
    private var totalAlbums = 0

    fun loadAlbums() {
        if (canLoadAlbums) {
            viewState.albumsLoadingInProgress.set(true)
            getCurrentUsersSavedAlbums.execute(currentAlbumsOffset)
                    .doFinally { viewState.albumsLoadingInProgress.set(false) }
                    .subscribeAndDisposeOnCleared({
                        viewState.albums.addAll(it.items.map(AlbumEntity::ui))
                        currentAlbumsOffset = it.offset + SpotifyApi.DEFAULT_LIMIT
                        totalAlbums = it.totalItems
                        viewState.albumsLoadingErrorOccurred.set(false)
                    }, getOnErrorWith {
                        viewState.albumsLoadingErrorOccurred.set(true)
                    })
        }
    }
}