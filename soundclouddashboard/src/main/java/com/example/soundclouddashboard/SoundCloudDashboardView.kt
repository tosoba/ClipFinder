package com.example.soundclouddashboard

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.Data
import com.example.core.android.model.soundcloud.SoundCloudPlaylist
import com.example.core.android.model.soundcloud.SoundCloudSystemPlaylist

data class SoundCloudDashboardViewState(
    val playlists: Data<SoundCloudPlaylists> = Data(value = SoundCloudPlaylists())
) : MvRxState

data class SoundCloudPlaylists(
    val regular: List<SoundCloudPlaylist> = emptyList(),
    val system: List<SoundCloudSystemPlaylist> = emptyList()
)
