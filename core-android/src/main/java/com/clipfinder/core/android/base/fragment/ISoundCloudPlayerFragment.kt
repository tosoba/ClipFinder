package com.clipfinder.core.android.base.fragment

import com.clipfinder.core.android.model.soundcloud.SoundCloudTrack

interface ISoundCloudPlayerFragment : IPlayerFragment {
    val lastPlayedTrack: SoundCloudTrack?
    fun loadTrack(track: SoundCloudTrack)
}
