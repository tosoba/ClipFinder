package com.example.coreandroid.base.trackvideos

import android.databinding.ObservableField
import android.util.Log
import com.example.coreandroid.base.model.BaseTrackUiModel
import com.example.coreandroid.base.vm.BaseViewModel
import com.example.there.domain.usecase.base.DeleteTrackUseCase
import com.example.there.domain.usecase.base.InsertTrackUseCase
import com.example.there.domain.usecase.base.IsTrackSavedUseCase
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
    ) = insertTrack.execute(track.domainEntity)
            .subscribeAndDisposeOnCleared({
                viewStates.peek().isSavedAsFavourite.set(true)
                viewState.isSavedAsFavourite.set(true)
            }, { Log.e(javaClass.name, "Insert error.") })

    fun deleteFavouriteTrack(
            track: Track
    ) = deleteTrack.execute(track.domainEntity)
            .subscribeAndDisposeOnCleared({
                viewStates.peek().isSavedAsFavourite.set(false)
                viewState.isSavedAsFavourite.set(false)
            }, { Log.e(javaClass.name, "Delete error.") })

    private fun loadTrackFavouriteState(
            track: Track
    ) = isTrackSaved.execute(track.domainEntity)
            .subscribeAndDisposeOnCleared {
                viewStates.peek().isSavedAsFavourite.set(it)
                viewState.isSavedAsFavourite.set(it)
            }
}