package com.example.soundclouddashboard

import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import com.example.coreandroid.model.soundcloud.SoundCloudPlaylist
import com.example.coreandroid.model.soundcloud.SoundCloudSystemPlaylist
import com.example.coreandroid.util.list.IdentifiableNamedObservableListItem
import com.example.coreandroid.util.list.ObservableSortedList

class SoundCloudDashboardView(
        val state: SoundCloudDashboardViewState,
        val dashboardAdapter: SoundCloudDashboardAdapter
)

class SoundCloudDashboardViewState(
        val loadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val playlists: ObservableList<SoundCloudPlaylist> =
                ObservableSortedList(SoundCloudPlaylist::class.java, IdentifiableNamedObservableListItem.sortedByNameCallback()),
        val systemPlaylists: ObservableList<SoundCloudSystemPlaylist> =
                ObservableSortedList(SoundCloudSystemPlaylist::class.java, IdentifiableNamedObservableListItem.sortedByNameCallback()),
        val loadingErrorOccurred: ObservableField<Boolean> = ObservableField(false)
)