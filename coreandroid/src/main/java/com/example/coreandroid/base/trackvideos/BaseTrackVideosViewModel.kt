package com.example.coreandroid.base.trackvideos

import androidx.databinding.ObservableField
import com.example.coreandroid.base.model.BaseTrackUiModel
import com.example.coreandroid.base.vm.BaseViewModel
import com.example.there.domain.usecase.base.DeleteTrackUseCase
import com.example.there.domain.usecase.base.InsertTrackUseCase
import com.example.there.domain.usecase.base.IsTrackSavedUseCase
import timber.log.Timber
import java.util.*

abstract class BaseTrackVideosViewModel<Track : BaseTrackUiModel<TrackEntity>, TrackEntity>(
        private val insertTrack: InsertTrackUseCase<TrackEntity>,
        private val deleteTrack: DeleteTrackUseCase<TrackEntity>,
        private val isTrackSaved: IsTrackSavedUseCase<TrackEntity>
) : BaseViewModel() {

    val viewState: TrackVideosViewState<Track> = TrackVideosViewState()

    private val viewStates: Stack<TrackVideosViewState<Track>> = Stack()

    private val lastTrack: Track? get() = viewState.track.get()

    fun onBackPressed(): Boolean = if (viewStates.size < 2) false
    else {
        viewStates.pop()
        val previous = viewStates.peek()
        viewState.track.set(previous.track.get())
        viewState.isSavedAsFavourite.set(previous.isSavedAsFavourite.get())
        true
    }

    fun updateState(track: Track) {
        if (track.id == lastTrack?.id) return
        viewState.track.set(track)
        viewStates.push(TrackVideosViewState(ObservableField(track)))
        loadTrackFavouriteState(track)
    }

    fun addFavouriteTrack(
            track: Track
    ) = insertTrack(track.domainEntity)
            .subscribeAndDisposeOnCleared({
                viewStates.peek().isSavedAsFavourite.set(true)
                viewState.isSavedAsFavourite.set(true)
            }, Timber::e)

    fun deleteFavouriteTrack(
            track: Track
    ) = deleteTrack(track.domainEntity)
            .subscribeAndDisposeOnCleared({
                viewStates.peek().isSavedAsFavourite.set(false)
                viewState.isSavedAsFavourite.set(false)
            }, Timber::e)

    private fun loadTrackFavouriteState(
            track: Track
    ) = isTrackSaved(track.id)
            .subscribeAndDisposeOnCleared {
                viewStates.peek().isSavedAsFavourite.set(it)
                viewState.isSavedAsFavourite.set(it)
            }
}