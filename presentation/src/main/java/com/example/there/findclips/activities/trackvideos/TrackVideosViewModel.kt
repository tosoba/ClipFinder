package com.example.there.findclips.activities.trackvideos

import android.util.Log
import com.example.there.domain.usecases.spotify.InsertTrack
import com.example.there.findclips.base.BaseViewModel
import com.example.there.findclips.model.entities.Track
import com.example.there.findclips.model.mappers.TrackEntityMapper

class TrackVideosViewModel(private val insertTrack: InsertTrack) : BaseViewModel() {
    val viewState = TrackVideosViewState()

    fun addFavouriteTrack(track: Track) {
        addDisposable(insertTrack.execute(TrackEntityMapper.mapBack(track)).subscribe({}, { Log.e(javaClass.name, "Insert error.") }))
    }
}