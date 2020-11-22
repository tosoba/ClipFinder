package com.example.soundclouddashboard.ui

import com.airbnb.mvrx.MvRxState
import com.clipfinder.core.android.soundcloud.model.SoundCloudPlaylistSelection
import com.example.core.android.model.Empty
import com.example.core.android.model.Loadable

data class SoundCloudDashboardState(val selections: Loadable<List<SoundCloudPlaylistSelection>> = Empty) : MvRxState
