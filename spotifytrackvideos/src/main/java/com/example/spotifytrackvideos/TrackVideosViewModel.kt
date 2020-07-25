package com.example.spotifytrackvideos

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.android.base.trackvideos.BaseTrackVideosViewModel
import com.example.core.android.base.trackvideos.TrackVideosViewState
import com.example.core.android.model.spotify.Track
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.usecase.spotify.DeleteTrack
import com.example.there.domain.usecase.spotify.InsertTrack
import com.example.there.domain.usecase.spotify.IsTrackSaved
import org.koin.android.ext.android.inject

class TrackVideosViewModel(
    initialState: TrackVideosViewState<Track>,
    insertTrack: InsertTrack,
    deleteTrack: DeleteTrack,
    isTrackSaved: IsTrackSaved
) : BaseTrackVideosViewModel<Track, TrackEntity>(
    initialState, insertTrack, deleteTrack, isTrackSaved
) {
    companion object : MvRxViewModelFactory<TrackVideosViewModel, TrackVideosViewState<Track>> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: TrackVideosViewState<Track>
        ): TrackVideosViewModel? {
            val insertTrack: InsertTrack by viewModelContext.activity.inject()
            val deleteTrack: DeleteTrack by viewModelContext.activity.inject()
            val isTrackSaved: IsTrackSaved by viewModelContext.activity.inject()
            return TrackVideosViewModel(state, insertTrack, deleteTrack, isTrackSaved)
        }
    }
}
