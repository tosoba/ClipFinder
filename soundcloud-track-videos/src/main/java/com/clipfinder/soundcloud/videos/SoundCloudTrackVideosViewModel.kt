package com.clipfinder.soundcloud.videos

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.android.base.trackvideos.BaseTrackVideosViewModel
import com.clipfinder.core.android.base.trackvideos.TrackVideosViewState
import com.clipfinder.core.android.model.soundcloud.SoundCloudTrack

class SoundCloudTrackVideosViewModel(initialState: TrackVideosViewState<SoundCloudTrack>) :
    BaseTrackVideosViewModel<SoundCloudTrack>(initialState) {
    companion object :
        MvRxViewModelFactory<
            SoundCloudTrackVideosViewModel, TrackVideosViewState<SoundCloudTrack>> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: TrackVideosViewState<SoundCloudTrack>
        ): SoundCloudTrackVideosViewModel = SoundCloudTrackVideosViewModel(state)
    }
}
