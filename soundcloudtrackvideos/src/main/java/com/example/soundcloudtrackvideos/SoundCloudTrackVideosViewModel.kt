package com.example.soundcloudtrackvideos

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.coreandroid.base.trackvideos.BaseTrackVideosViewModel
import com.example.coreandroid.base.trackvideos.TrackVideosViewState
import com.example.coreandroid.model.soundcloud.SoundCloudTrack
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.usecase.soundcloud.DeleteSoundCloudTrack
import com.example.there.domain.usecase.soundcloud.InsertSoundCloudTrack
import com.example.there.domain.usecase.soundcloud.IsSoundCloudTrackSaved
import org.koin.android.ext.android.inject

class SoundCloudTrackVideosViewModel(
        initialState: TrackVideosViewState<SoundCloudTrack>,
        insertSoundCloudTrack: InsertSoundCloudTrack,
        isSoundCloudTrackSaved: IsSoundCloudTrackSaved,
        deleteSoundCloudTrack: DeleteSoundCloudTrack
) : BaseTrackVideosViewModel<SoundCloudTrack, SoundCloudTrackEntity>(
        initialState, insertSoundCloudTrack, deleteSoundCloudTrack, isSoundCloudTrackSaved
) {
    companion object : MvRxViewModelFactory<SoundCloudTrackVideosViewModel, TrackVideosViewState<SoundCloudTrack>> {
        override fun create(viewModelContext: ViewModelContext, state: TrackVideosViewState<SoundCloudTrack>): SoundCloudTrackVideosViewModel? {
            val insertSoundCloudTrack: InsertSoundCloudTrack by viewModelContext.activity.inject()
            val isSoundCloudTrackSaved: IsSoundCloudTrackSaved by viewModelContext.activity.inject()
            val deleteSoundCloudTrack: DeleteSoundCloudTrack by viewModelContext.activity.inject()
            return SoundCloudTrackVideosViewModel(state, insertSoundCloudTrack, isSoundCloudTrackSaved, deleteSoundCloudTrack)
        }
    }
}