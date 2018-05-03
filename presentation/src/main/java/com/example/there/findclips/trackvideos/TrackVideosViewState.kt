package com.example.there.findclips.trackvideos

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.example.there.findclips.entities.Track
import com.example.there.findclips.entities.Video

data class TrackVideosViewState(
        val track: Track,
        val videos: ObservableArrayList<Video> = ObservableArrayList(),
        val videosLoadingInProgress: ObservableField<Boolean> = ObservableField(false)
)