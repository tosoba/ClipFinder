package com.example.soundcloudtrackvideos

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.coreandroid.base.trackvideos.BaseTrackVideosViewModel
import com.example.coreandroid.mapper.soundcloud.ui
import com.example.coreandroid.model.soundcloud.SoundCloudTrack
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.usecase.soundcloud.DeleteSoundCloudTrack
import com.example.there.domain.usecase.soundcloud.GetSimilarTracks
import com.example.there.domain.usecase.soundcloud.InsertSoundCloudTrack
import com.example.there.domain.usecase.soundcloud.IsSoundCloudTrackSaved
import javax.inject.Inject

class SoundCloudTrackVideosViewModel @Inject constructor(
        insertSoundCloudTrack: InsertSoundCloudTrack,
        isSoundCloudTrackSaved: IsSoundCloudTrackSaved,
        deleteSoundCloudTrack: DeleteSoundCloudTrack,
        private val getSimilarTracks: GetSimilarTracks
) : BaseTrackVideosViewModel<SoundCloudTrack, SoundCloudTrackEntity>(insertSoundCloudTrack, deleteSoundCloudTrack, isSoundCloudTrackSaved) {

    private val _similarTracks: MutableLiveData<List<SoundCloudTrack>> = MutableLiveData()

    val similarTracks: LiveData<List<SoundCloudTrack>> = _similarTracks

    fun loadSimilarTracks(id: String) {
        getSimilarTracks.execute(id).subscribeAndDisposeOnCleared({
            _similarTracks.value = it.map(SoundCloudTrackEntity::ui)
        }, ::onError)
    }
}