package com.example.soundcloudtrackvideos

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.coreandroid.base.vm.BaseViewModel
import com.example.coreandroid.mapper.soundcloud.ui
import com.example.coreandroid.model.soundcloud.SoundCloudTrack
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.usecase.soundcloud.GetSimilarTracks
import javax.inject.Inject

class SoundCloudTrackVideosViewModel @Inject constructor(
        private val getSimilarTracks: GetSimilarTracks
) : BaseViewModel() {

    val viewState = SoundCloudTrackVideosViewState()

    private val _similarTracks: MutableLiveData<List<SoundCloudTrack>> = MutableLiveData()

    val similarTracks: LiveData<List<SoundCloudTrack>> = _similarTracks

    fun loadSimilarTracks(id: String) {
        getSimilarTracks.execute(id).subscribeAndDisposeOnCleared({
            _similarTracks.value = it.map(SoundCloudTrackEntity::ui)
        }, ::onError)
    }
}