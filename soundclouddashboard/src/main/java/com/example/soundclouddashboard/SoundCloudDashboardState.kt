package com.example.soundclouddashboard

import com.airbnb.mvrx.MvRxState
import com.clipfinder.core.android.soundcloud.model.SoundCloudPlaylistSelection
import com.clipfinder.core.model.Empty
import com.clipfinder.core.model.Loadable

data class SoundCloudDashboardState(val selections: Loadable<List<SoundCloudPlaylistSelection>> = Empty) : MvRxState
