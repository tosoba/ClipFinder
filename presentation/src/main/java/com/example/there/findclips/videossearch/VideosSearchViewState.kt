package com.example.there.findclips.videossearch

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.example.there.findclips.entities.Video

data class VideosSearchViewState(
        val videos: ObservableArrayList<Video> = ObservableArrayList(),
        val videosLoadingInProgress: ObservableField<Boolean> = ObservableField(false)
)