package com.example.there.findclips.soundcloud.dashboard

import com.example.there.domain.usecase.soundcloud.DiscoverSoundCloud
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.model.entity.soundcloud.ui
import javax.inject.Inject

class SoundCloudDashboardViewModel @Inject constructor(
        private val discoverSoundCloud: DiscoverSoundCloud
) : BaseViewModel() {

    val viewState = SoundCloudDashboardViewState()

    fun loadPlaylists() {
        viewState.loadingInProgress.set(true)
        addDisposable(discoverSoundCloud.execute()
                .doFinally { viewState.loadingInProgress.set(false) }
                .subscribe({ discoverEntity ->
                    viewState.playlists.addAll(discoverEntity.playlists.map { it.ui })
                    viewState.systemPlaylists.addAll(discoverEntity.systemPlaylists.map { it.ui })
                }, ::onError))
    }
}