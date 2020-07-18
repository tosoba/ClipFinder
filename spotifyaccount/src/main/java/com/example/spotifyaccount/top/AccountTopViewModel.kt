package com.example.spotifyaccount.top

import com.example.core.SpotifyDefaults
import com.example.coreandroid.base.vm.BaseViewModel
import com.example.coreandroid.mapper.spotify.ui
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.usecase.spotify.GetCurrentUsersTopArtists
import com.example.there.domain.usecase.spotify.GetCurrentUsersTopTracks

class AccountTopViewModel(
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
            getCurrentUsersTopTracks(currentTracksOffset)
                .takeSuccessOnly()
                .doFinally { viewState.tracksLoadingInProgress.set(false) }
                .subscribeAndDisposeOnCleared({
                    viewState.topTracks.addAll(it.items.map(TrackEntity::ui))
                    currentTracksOffset = it.offset + SpotifyDefaults.LIMIT
                    totalTracks = it.total
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
            getCurrentUsersTopArtists(currentArtistsOffset)
                .takeSuccessOnly()
                .doFinally { viewState.artistsLoadingInProgress.set(false) }
                .subscribeAndDisposeOnCleared({
                    viewState.artists.addAll(it.items.map(ArtistEntity::ui))
                    currentArtistsOffset = it.offset + SpotifyDefaults.LIMIT
                    totalArtists = it.total
                    viewState.tracksLoadingErrorOccurred.set(false)
                }, getOnErrorWith {
                    viewState.tracksLoadingErrorOccurred.set(true)
                })
        }
    }
}
