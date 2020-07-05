package com.example.coreandroid.base.fragment

import com.example.coreandroid.model.soundcloud.SoundCloudTrack

interface ISoundCloudPlayerFragment : IPlayerFragment {
    val lastPlayedTrack: SoundCloudTrack?
    fun loadTrack(track: SoundCloudTrack)
}
