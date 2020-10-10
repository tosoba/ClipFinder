package com.example.core.android.base.trackvideos

import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.model.DataList

abstract class BaseTrackVideosViewModel<Track>(
    initialState: TrackVideosViewState<Track>
) : MvRxViewModel<TrackVideosViewState<Track>>(initialState) {

    fun onBackPressed() = withState { state ->
        if (state.tracks.value.size < 2) {
            setState { copy(tracks = DataList(emptyList())) }
            return@withState
        }

        setState { copy(tracks = DataList(tracks.value.take(tracks.value.size - 1))) }
    }

    fun updateTrack(track: Track) = setState { copy(tracks = tracks.copyWithNewItems(track)) }
}
