package com.example.spotifyaccount.playlist

import androidx.lifecycle.MutableLiveData
import com.example.core.SpotifyDefaults
import com.example.core.android.base.vm.BaseViewModel
import com.example.core.android.mapper.spotify.ui
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.usecase.spotify.GetCurrentUsersPlaylists

class AccountPlaylistsViewModel(
    private val getCurrentUsersPlaylists: GetCurrentUsersPlaylists
) : BaseViewModel() {

    lateinit var viewState: AccountPlaylistViewState

    val loadedFlag: MutableLiveData<Unit> = MutableLiveData()

    private val canLoadPlaylists
        get() = viewState.playlistsLoadingInProgress.get() == false &&
            viewState.userLoggedIn.get() == true &&
            (currentOffset == 0 || (currentOffset < totalItems))

    private var currentOffset = 0
    private var totalItems = 0

    fun loadPlaylists() {
        if (canLoadPlaylists) {
            viewState.playlistsLoadingInProgress.set(true)
            getCurrentUsersPlaylists(currentOffset)
                .takeSuccessOnly()
                .doFinally { viewState.playlistsLoadingInProgress.set(false) }
                .subscribeAndDisposeOnCleared({
                    viewState.playlists.addAll(it.items.map(PlaylistEntity::ui))
                    currentOffset = it.offset + SpotifyDefaults.LIMIT
                    totalItems = it.total
                    loadedFlag.value = Unit
                }, ::onError)
        }
    }
}