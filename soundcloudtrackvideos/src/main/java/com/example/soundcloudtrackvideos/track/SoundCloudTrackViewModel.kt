package com.example.soundcloudtrackvideos.track

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.soundcloud.usecase.GetSimilarTracks
import com.example.core.android.base.viewmodel.MvRxViewModel
import com.clipfinder.core.model.Ready
import com.example.core.android.model.soundcloud.SoundCloudTrack
import org.koin.android.ext.android.get

class SoundCloudTrackViewModel(
    initialState: SoundCloudTrackViewState,
    private val getSimilarTracks: GetSimilarTracks
) : MvRxViewModel<SoundCloudTrackViewState>(initialState) {

    fun loadSimilarTracks(id: Int) {
        getSimilarTracks(args = id.toString())
            .subscribe(
                { setState { copy(similarTracks = Ready(it.map(::SoundCloudTrack))) } },
                { setState { copy(similarTracks = similarTracks.copyWithError(it)) } }
            )
            .disposeOnClear()
    }

    fun clearTracksError() = clearErrorIn(SoundCloudTrackViewState::similarTracks) { copy(similarTracks = it) }

    companion object : MvRxViewModelFactory<SoundCloudTrackViewModel, SoundCloudTrackViewState> {
        override fun create(
            viewModelContext: ViewModelContext, state: SoundCloudTrackViewState
        ): SoundCloudTrackViewModel = SoundCloudTrackViewModel(
            state,
            viewModelContext.activity.get()
        )
    }
}