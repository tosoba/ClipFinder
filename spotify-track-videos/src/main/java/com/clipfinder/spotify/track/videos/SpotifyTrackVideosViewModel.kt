package com.clipfinder.spotify.track.videos

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.android.base.trackvideos.BaseTrackVideosViewModel
import com.clipfinder.core.android.base.trackvideos.TrackVideosViewState
import com.clipfinder.core.android.spotify.model.Track

class SpotifyTrackVideosViewModel(
    initialState: TrackVideosViewState<Track>
) : BaseTrackVideosViewModel<Track>(initialState) {
    companion object : MvRxViewModelFactory<SpotifyTrackVideosViewModel, TrackVideosViewState<Track>> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: TrackVideosViewState<Track>
        ): SpotifyTrackVideosViewModel = SpotifyTrackVideosViewModel(state)
    }
}
