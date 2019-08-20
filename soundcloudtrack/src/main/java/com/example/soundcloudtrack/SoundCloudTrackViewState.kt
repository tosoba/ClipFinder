package com.example.soundcloudtrack

import com.airbnb.mvrx.MvRxState
import com.example.coreandroid.model.DataList
import com.example.coreandroid.model.soundcloud.SoundCloudTrack

data class SoundCloudTrackViewState(val similarTracks: DataList<SoundCloudTrack> = DataList()) : MvRxState