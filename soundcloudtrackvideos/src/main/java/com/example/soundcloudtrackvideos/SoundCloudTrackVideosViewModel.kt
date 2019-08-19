package com.example.soundcloudtrackvideos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.coreandroid.base.trackvideos.BaseTrackVideosViewModel
import com.example.coreandroid.base.trackvideos.TrackVideosViewState
import com.example.coreandroid.mapper.soundcloud.ui
import com.example.coreandroid.model.soundcloud.SoundCloudTrack
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.usecase.soundcloud.DeleteSoundCloudTrack
import com.example.there.domain.usecase.soundcloud.GetSimilarTracks
import com.example.there.domain.usecase.soundcloud.InsertSoundCloudTrack
import com.example.there.domain.usecase.soundcloud.IsSoundCloudTrackSaved
import org.koin.android.ext.android.inject

class SoundCloudTrackVideosViewModel(
        initialState: TrackVideosViewState<SoundCloudTrack>,
        insertSoundCloudTrack: InsertSoundCloudTrack,
        isSoundCloudTrackSaved: IsSoundCloudTrackSaved,
        deleteSoundCloudTrack: DeleteSoundCloudTrack,
        private val getSimilarTracks: GetSimilarTracks
) : BaseTrackVideosViewModel<SoundCloudTrack, SoundCloudTrackEntity>(initialState, insertSoundCloudTrack, deleteSoundCloudTrack, isSoundCloudTrackSaved) {

    //TODO: turn this into a SoundCloudTrackFragment that will handle loading similar tracks
    private val _similarTracks: MutableLiveData<List<SoundCloudTrack>> = MutableLiveData()
    val similarTracks: LiveData<List<SoundCloudTrack>> = _similarTracks

    fun loadSimilarTracks(id: String) {
        getSimilarTracks(id).subscribeAndDisposeOnCleared({
            _similarTracks.value = it.map(SoundCloudTrackEntity::ui)
        }, ::onError)
    }

    companion object : MvRxViewModelFactory<SoundCloudTrackVideosViewModel, TrackVideosViewState<SoundCloudTrack>> {
        override fun create(viewModelContext: ViewModelContext, state: TrackVideosViewState<SoundCloudTrack>): SoundCloudTrackVideosViewModel? {
            val insertSoundCloudTrack: InsertSoundCloudTrack by viewModelContext.activity.inject()
            val isSoundCloudTrackSaved: IsSoundCloudTrackSaved by viewModelContext.activity.inject()
            val deleteSoundCloudTrack: DeleteSoundCloudTrack by viewModelContext.activity.inject()
            return SoundCloudTrackVideosViewModel(state, insertSoundCloudTrack, isSoundCloudTrackSaved, deleteSoundCloudTrack)
        }
    }
}