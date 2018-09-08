package com.example.there.findclips.fragment.trackvideos

import android.databinding.ObservableField
import android.util.Log
import com.example.there.domain.usecase.spotify.InsertTrack
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.model.entity.Track
import com.example.there.findclips.model.mapper.TrackEntityMapper
import java.util.*
import javax.inject.Inject

class TrackVideosViewModel @Inject constructor(
        private val insertTrack: InsertTrack
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
        true
    }

    fun updateState(track: Track) {
        if (track.id == lastTrack?.id) return
        viewState.track.set(track)
        viewStates.push(TrackVideosViewState(ObservableField(track)))
    }

    fun addFavouriteTrack(track: Track) {
        addDisposable(insertTrack.execute(TrackEntityMapper.mapBack(track)).subscribe({}, { Log.e(javaClass.name, "Insert error.") }))
    }
}