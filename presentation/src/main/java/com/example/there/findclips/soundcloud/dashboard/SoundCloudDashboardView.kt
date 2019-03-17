package com.example.there.findclips.soundcloud.dashboard

import android.databinding.ObservableField
import android.databinding.ObservableList

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