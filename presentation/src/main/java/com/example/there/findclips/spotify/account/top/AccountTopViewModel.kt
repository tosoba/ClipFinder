package com.example.there.findclips.spotify.account.top

import com.example.there.data.api.spotify.SpotifyApi
import com.example.there.domain.usecase.spotify.GetCurrentUsersTopArtists
import com.example.there.domain.usecase.spotify.GetCurrentUsersTopTracks
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.model.mapper.ArtistEntityMapper
import com.example.there.findclips.model.mapper.TrackEntityMapper
import javax.inject.Inject

class AccountTopViewModel @Inject constructor(
        private val getCurrentUsersTopTracks: GetCurrentUsersTopTracks,
        private val getCurrentUsersTopArtists: GetCurrentUsersTopArtists
) : BaseViewModel() {

    lateinit var viewState: AccountTopViewState

    private val canLoadTracks: Boolean
        get() = viewState.tracksLoadingInProgress.get() == false &&
                viewState.userLoggedIn.get() == true &&
                (currentTracksOffset == 0 || (currentTracksOffset < totalTracks))

    private var currentTracksOffset = 0
    private var totalTracks = 0

    fun loadTracks() {
        if (canLoadTracks) {
            viewState.tracksLoadingInProgress.set(true)
            getCurrentUsersTopTracks.execute(currentTracksOffset)
                    .doFinally { viewState.tracksLoadingInProgress.set(false) }
                    .subscribeAndDisposeOnCleared({
                        viewState.topTracks.addAll(it.items.map(TrackEntityMapper::mapFrom))
                        currentTracksOffset = it.offset + SpotifyApi.DEFAULT_LIMIT
                        totalTracks = it.totalItems
                        viewState.tracksLoadingErrorOccurred.set(false)
                    }, getOnErrorWith {
                        viewState.tracksLoadingErrorOccurred.set(true)
                    })
        }
    }

    private val canLoadArtists: Boolean
        get() = viewState.artistsLoadingInProgress.get() == false &&
                viewState.userLoggedIn.get() == true &&
                (currentArtistsOffset == 0 || (currentArtistsOffset < totalArtists))

    private var currentArtistsOffset = 0
    private var totalArtists = 0

    fun loadArtists() {
        if (canLoadArtists) {
            viewState.artistsLoadingInProgress.set(true)
            getCurrentUsersTopArtists.execute(currentArtistsOffset)
                    .doFinally { viewState.artistsLoadingInProgress.set(false) }
                    .subscribeAndDisposeOnCleared({
                        viewState.artists.addAll(it.items.map(ArtistEntityMapper::mapFrom))
                        currentArtistsOffset = it.offset + SpotifyApi.DEFAULT_LIMIT
                        totalArtists = it.totalItems
                        viewState.tracksLoadingErrorOccurred.set(false)
                    }, getOnErrorWith {
                        viewState.tracksLoadingErrorOccurred.set(true)
                    })
        }
    }
}