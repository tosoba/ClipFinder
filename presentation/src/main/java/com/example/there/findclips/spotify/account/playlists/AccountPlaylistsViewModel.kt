package com.example.there.findclips.spotify.account.playlists

import android.arch.lifecycle.MutableLiveData
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.usecase.spotify.GetCurrentUsersPlaylists
import javax.inject.Inject

class AccountPlaylistsViewModel @Inject constructor(
        private val getCurrentUsersPlaylists: GetCurrentUsersPlaylists
) : com.example.coreandroid.base.vm.BaseViewModel() {

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
            getCurrentUsersPlaylists.execute(currentOffset)
                    .doFinally { viewState.playlistsLoadingInProgress.set(false) }
                    .subscribeAndDisposeOnCleared({
                        viewState.playlists.addAll(it.items.map(PlaylistEntity::ui))
                        currentOffset = it.offset + SpotifyApi.DEFAULT_LIMIT
                        totalItems = it.totalItems
                        loadedFlag.value = Unit
                    }, ::onError)
        }
    }
}