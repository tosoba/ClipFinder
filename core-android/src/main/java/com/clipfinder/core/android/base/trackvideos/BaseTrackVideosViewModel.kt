package com.clipfinder.core.android.base.trackvideos

import com.clipfinder.core.android.base.viewmodel.MvRxViewModel

abstract class BaseTrackVideosViewModel<Track>(
    initialState: TrackVideosViewState<Track>,
) : MvRxViewModel<TrackVideosViewState<Track>>(initialState) {
    fun updateTrack(track: Track) = setState { copy(tracks = tracks + track) }

    fun onBackPressed() = withState { state ->
        if (state.tracks.size < 2) {
            setState { copy(tracks = emptyList()) }
            return@withState
        }

        setState { copy(tracks = tracks.dropLast(1)) }
    }
}
