package com.example.core.android.base.handler

import com.example.core.android.model.soundcloud.SoundCloudTrack

interface SoundCloudPlayerController {
    fun loadTrack(track: SoundCloudTrack)
}
