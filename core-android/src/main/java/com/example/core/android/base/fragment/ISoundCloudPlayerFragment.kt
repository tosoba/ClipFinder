package com.example.core.android.base.fragment

import com.example.core.android.model.soundcloud.SoundCloudTrack

interface ISoundCloudPlayerFragment : IPlayerFragment {
    val lastPlayedTrack: SoundCloudTrack?
    fun loadTrack(track: SoundCloudTrack)
}
