package com.example.soundclouddashboard

import com.example.coreandroid.base.vm.BaseViewModel
import com.example.coreandroid.mapper.soundcloud.ui
import com.example.there.domain.usecase.soundcloud.DiscoverSoundCloud
import javax.inject.Inject

class SoundCloudDashboardViewModel @Inject constructor(
        private val discoverSoundCloud: DiscoverSoundCloud
) : BaseViewModel() {

    val viewState = SoundCloudDashboardViewState()

    fun loadPlaylists() {
        viewState.loadingInProgress.set(true)
        discoverSoundCloud.execute()
                .doFinally { viewState.loadingInProgress.set(false) }
                .subscribeAndDisposeOnCleared({ discoverEntity ->
                    viewState.playlists.addAll(discoverEntity.playlists.map { it.ui })
                    viewState.systemPlaylists.addAll(discoverEntity.systemPlaylists.map { it.ui })
                    viewState.loadingErrorOccurred.set(false)
                }, getOnErrorWith {
                    viewState.loadingErrorOccurred.set(true)
                })
    }
}