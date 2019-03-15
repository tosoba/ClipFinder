package com.example.there.findclips.soundcloud.playlist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.usecase.soundcloud.GetTracks
import com.example.there.domain.usecase.soundcloud.GetTracksFromPlaylist
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.model.entity.soundcloud.SoundCloudTrack
import com.example.there.findclips.model.mapper.soundcloud.ui
import javax.inject.Inject

class SoundCloudPlaylistViewModel @Inject constructor(
        private val getTracksFromPlaylist: GetTracksFromPlaylist,
        private val getTracks: GetTracks
) : BaseViewModel() {

    val viewState = SoundCloudPlaylistViewState()

    private val mutableTracks: MutableLiveData<List<SoundCloudTrack>> = MutableLiveData()
    val tracks: LiveData<List<SoundCloudTrack>>
        get() = mutableTracks

    fun loadTracksWithIds(ids: List<String>) {
        viewState.loadingInProgress.set(true)
        getTracks.execute(ids)
                .doFinally { viewState.loadingInProgress.set(false) }
                .subscribeAndDisposeOnCleared({
                    mutableTracks.value = it.map(SoundCloudTrackEntity::ui)
                }, ::onError)
    }

    fun loadTracksFromPlaylist(id: String) {
        viewState.loadingInProgress.set(true)
        getTracksFromPlaylist.execute(id)
                .doFinally { viewState.loadingInProgress.set(false) }
                .subscribeAndDisposeOnCleared({
                    mutableTracks.value = it.map(SoundCloudTrackEntity::ui)
                }, ::onError)
    }
}