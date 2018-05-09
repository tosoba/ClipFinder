package com.example.there.findclips.util

import com.example.there.findclips.entities.Video

interface VideoPlayerHost {
    fun playVideo(video: Video)
}