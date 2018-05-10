package com.example.there.findclips.player

import com.example.there.findclips.entities.Video

interface PlayerHost {
    fun playVideo(video: Video)
}