package com.example.soundclouddashboard.ui

import com.airbnb.mvrx.MvRxState
import com.clipfinder.core.android.soundcloud.model.SoundCloudPlaylistSelection
import com.example.core.android.model.Data
import com.example.core.android.model.DataList
import com.example.core.android.model.soundcloud.SoundCloudPlaylist
import com.example.core.android.model.soundcloud.SoundCloudSystemPlaylist

data class SoundCloudDashboardState(
    val selections: DataList<SoundCloudPlaylistSelection> = DataList()
) : MvRxState
