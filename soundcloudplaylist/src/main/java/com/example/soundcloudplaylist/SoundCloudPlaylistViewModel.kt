package com.example.soundcloudplaylist

import com.example.coreandroid.base.playlist.BasePlaylistViewModel
import com.example.coreandroid.mapper.soundcloud.ui
import com.example.coreandroid.model.soundcloud.SoundCloudTrack
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.usecase.soundcloud.GetTracks
import com.example.there.domain.usecase.soundcloud.GetTracksFromPlaylist

class SoundCloudPlaylistViewModel(
        private val getTracksFromPlaylist: GetTracksFromPlaylist,
        private val getTracks: GetTracks
) : BasePlaylistViewModel<SoundCloudTrack>() {

    fun loadTracksWithIds(ids: List<String>) {
        viewState.loadingInProgress.set(true)
        getTracks(ids)
                .doFinally { viewState.loadingInProgress.set(false) }
                .subscribeAndDisposeOnCleared({
                    mutableTracks.value = it.map(SoundCloudTrackEntity::ui)
                }, ::onError)
    }

    fun loadTracksFromPlaylist(id: String) {
        viewState.loadingInProgress.set(true)
        getTracksFromPlaylist(id)
                .doFinally { viewState.loadingInProgress.set(false) }
                .subscribeAndDisposeOnCleared({
                    mutableTracks.value = it.map(SoundCloudTrackEntity::ui)
                }, ::onError)
    }
}