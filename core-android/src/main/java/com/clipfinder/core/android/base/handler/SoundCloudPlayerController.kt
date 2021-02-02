package com.clipfinder.core.android.base.handler

import com.clipfinder.core.android.model.soundcloud.SoundCloudTrack

interface SoundCloudPlayerController {
    fun loadTrack(track: SoundCloudTrack)
}
