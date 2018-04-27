package com.example.there.findclips.videos

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.example.there.domain.entities.videos.VideoEntity

data class VideosViewState(
        val videos: ObservableArrayList<VideoEntity> = ObservableArrayList(),
        val videosLoadingInProgress: ObservableField<Boolean> = ObservableField(false)
)