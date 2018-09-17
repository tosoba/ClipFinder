package com.example.there.findclips.fragment.account.playlists

import android.arch.lifecycle.MutableLiveData
import com.example.there.data.api.spotify.SpotifyApi
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.usecase.spotify.GetCurrentUsersPlaylists
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.model.mapper.PlaylistEntityMapper
import javax.inject.Inject

class AccountPlaylistsViewModel @Inject constructor(
        private val getCurrentUsersPlaylists: GetCurrentUsersPlaylists
) : BaseViewModel() {

    lateinit var viewState: AccountPlaylistViewState

    val loadedFlag: MutableLiveData<Unit> = MutableLiveData()

    private var currentOffset = 0
    private var totalItems = 0

    fun loadPlaylists(accessToken: AccessTokenEntity) {
        if (canLoadPlaylists) {
            viewState.playlistsLoadingInProgress.set(true)
            addDisposable(getCurrentUsersPlaylists.execute(accessToken, currentOffset)
                    .doFinally { viewState.playlistsLoadingInProgress.set(false) }
                    .subscribe({
                        viewState.playlists.addAll(it.items.map(PlaylistEntityMapper::mapFrom))
                        currentOffset = it.offset + SpotifyApi.DEFAULT_LIMIT.toInt()
                        totalItems = it.totalItems
                        loadedFlag.value = Unit
                    }, ::onError))
        }
    }

    private val canLoadPlaylists
        get() = viewState.playlistsLoadingInProgress.get() == false &&
                viewState.userLoggedIn.get() == true &&
                (currentOffset == 0 || (currentOffset < totalItems))
}