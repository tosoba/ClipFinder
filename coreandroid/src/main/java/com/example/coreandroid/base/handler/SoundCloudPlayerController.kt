package com.example.coreandroid.base.handler

import com.example.coreandroid.model.soundcloud.SoundCloudTrack

interface SoundCloudPlayerController {
    fun loadTrack(track: SoundCloudTrack)
}
