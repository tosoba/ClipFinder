package com.example.soundcloudtrackvideos.track

import com.airbnb.mvrx.MvRxState
import com.clipfinder.core.model.Empty
import com.clipfinder.core.model.Loadable
import com.example.core.android.model.soundcloud.SoundCloudTrack

data class SoundCloudTrackViewState(val similarTracks: Loadable<List<SoundCloudTrack>> = Empty) : MvRxState
