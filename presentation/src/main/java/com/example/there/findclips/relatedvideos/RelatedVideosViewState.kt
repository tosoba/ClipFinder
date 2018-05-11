package com.example.there.findclips.relatedvideos

import android.databinding.ObservableArrayList
import com.example.there.findclips.entities.Video

data class RelatedVideosViewState(
        val videos: ObservableArrayList<Video> = ObservableArrayList()
)