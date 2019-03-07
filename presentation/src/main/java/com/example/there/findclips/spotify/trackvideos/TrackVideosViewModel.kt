package com.example.there.findclips.spotify.trackvideos

import android.databinding.ObservableField
import android.util.Log
import com.example.there.domain.usecase.spotify.DeleteTrack
import com.example.there.domain.usecase.spotify.InsertTrack
import com.example.there.domain.usecase.spotify.IsTrackSaved
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.model.entity.Track
import com.example.there.findclips.model.mapper.TrackEntityMapper
import java.util.*
import javax.inject.Inject

class TrackVideosViewModel @Inject constructor(
        private val insertTrack: InsertTrack,
        private val deleteTrack: DeleteTrack,
        private val isTrackSaved: IsTrackSaved
) : BaseViewModel() {

    val viewState = TrackVideosViewState()

    private val viewStates: Stack<TrackVideosViewState> = Stack()

    private val lastTrack: Track?
        get() = viewState.track.get()

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
    ) = addDisposable(insertTrack.execute(TrackEntityMapper.mapBack(track))
            .subscribe({
                viewStates.peek().isSavedAsFavourite.set(true)
                viewState.isSavedAsFavourite.set(true)
            }, { Log.e(javaClass.name, "Insert error.") }))

    fun deleteFavouriteTrack(
            track: Track
    ) = addDisposable(deleteTrack.execute(TrackEntityMapper.mapBack(track))
            .subscribe({
                viewStates.peek().isSavedAsFavourite.set(false)
                viewState.isSavedAsFavourite.set(false)
            }, { Log.e(javaClass.name, "Delete error.") }))

    private fun loadTrackFavouriteState(
            track: Track
    ) = addDisposable(isTrackSaved.execute(TrackEntityMapper.mapBack(track))
            .subscribe({
                viewStates.peek().isSavedAsFavourite.set(it)
                viewState.isSavedAsFavourite.set(it)
            }, {}))
}