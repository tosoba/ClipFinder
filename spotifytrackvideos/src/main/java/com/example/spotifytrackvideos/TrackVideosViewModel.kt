package com.example.spotifytrackvideos

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.android.base.trackvideos.BaseTrackVideosViewModel
import com.example.core.android.base.trackvideos.TrackVideosViewState
import com.example.core.android.spotify.model.Track

class TrackVideosViewModel(
    initialState: TrackVideosViewState<Track>
) : BaseTrackVideosViewModel<Track>(initialState) {
    companion object : MvRxViewModelFactory<TrackVideosViewModel, TrackVideosViewState<Track>> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: TrackVideosViewState<Track>
        ): TrackVideosViewModel? = TrackVideosViewModel(state)
    }
}
