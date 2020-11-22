package com.example.soundcloudtrackvideos.track

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.Empty
import com.example.core.android.model.Loadable
import com.example.core.android.model.soundcloud.SoundCloudTrack

data class SoundCloudTrackViewState(val similarTracks: Loadable<List<SoundCloudTrack>> = Empty) : MvRxState
