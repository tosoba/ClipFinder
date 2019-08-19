package com.example.soundcloudtrack

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.coreandroid.base.vm.MvRxViewModel
import com.example.coreandroid.mapper.soundcloud.ui
import com.example.coreandroid.model.DataList
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.usecase.soundcloud.GetSimilarTracks
import org.koin.android.ext.android.inject

class SoundCloudTrackViewModel(
        initialState: SoundCloudTrackViewState,
        private val getSimilarTracks: GetSimilarTracks
) : MvRxViewModel<SoundCloudTrackViewState>(initialState) {

    fun loadSimilarTracks(id: String) {
        getSimilarTracks(args = id)
                .subscribe(
                        { setState { copy(similarTracks = DataList(it.map(SoundCloudTrackEntity::ui))) } },
                        { setState { copy(similarTracks = similarTracks.copyWithError(it)) } }
                )
                .disposeOnClear()
    }

    companion object : MvRxViewModelFactory<SoundCloudTrackViewModel, SoundCloudTrackViewState> {
        override fun create(viewModelContext: ViewModelContext, state: SoundCloudTrackViewState): SoundCloudTrackViewModel? {
            val getSimilarTracks: GetSimilarTracks by viewModelContext.activity.inject()
            return SoundCloudTrackViewModel(state, getSimilarTracks)
        }
    }
}