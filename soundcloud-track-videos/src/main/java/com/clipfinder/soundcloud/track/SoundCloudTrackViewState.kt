package com.clipfinder.soundcloud.track

import com.airbnb.mvrx.MvRxState
import com.clipfinder.core.android.model.soundcloud.SoundCloudTrack
import com.clipfinder.core.model.Empty
import com.clipfinder.core.model.Loadable

data class SoundCloudTrackViewState(val similarTracks: Loadable<List<SoundCloudTrack>> = Empty) :
    MvRxState
