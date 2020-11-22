package com.example.soundcloudtrackvideos.track

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.mapper.soundcloud.ui
import com.example.core.android.model.Ready
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.usecase.soundcloud.GetSimilarTracks
import org.koin.android.ext.android.get

class SoundCloudTrackViewModel(
    initialState: SoundCloudTrackViewState,
    private val getSimilarTracks: GetSimilarTracks
) : MvRxViewModel<SoundCloudTrackViewState>(initialState) {

    fun loadSimilarTracks(id: String) {
        getSimilarTracks(args = id)
            .subscribe(
                { setState { copy(similarTracks = Ready(it.map(SoundCloudTrackEntity::ui))) } },
                { setState { copy(similarTracks = similarTracks.copyWithError(it)) } }
            )
            .disposeOnClear()
    }

    fun clearTracksError() = clearErrorIn(SoundCloudTrackViewState::similarTracks) { copy(similarTracks = it) }

    companion object : MvRxViewModelFactory<SoundCloudTrackViewModel, SoundCloudTrackViewState> {
        override fun create(
            viewModelContext: ViewModelContext, state: SoundCloudTrackViewState
        ): SoundCloudTrackViewModel? = SoundCloudTrackViewModel(state, viewModelContext.activity.get())
    }
}