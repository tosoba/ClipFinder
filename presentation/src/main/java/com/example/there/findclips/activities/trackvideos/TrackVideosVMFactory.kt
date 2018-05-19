package com.example.there.findclips.activities.trackvideos

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecases.spotify.InsertTrack

@Suppress("UNCHECKED_CAST")
class TrackVideosVMFactory(private val insertTrack: InsertTrack): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = TrackVideosViewModel(insertTrack) as T
}