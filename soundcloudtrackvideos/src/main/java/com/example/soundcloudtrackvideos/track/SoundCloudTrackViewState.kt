package com.example.soundcloudtrackvideos.track

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.DataList
import com.example.core.android.model.soundcloud.SoundCloudTrack

data class SoundCloudTrackViewState(val similarTracks: DataList<SoundCloudTrack> = DataList()) : MvRxState
