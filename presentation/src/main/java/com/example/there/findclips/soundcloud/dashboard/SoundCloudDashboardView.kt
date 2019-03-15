package com.example.there.findclips.soundcloud.dashboard

import android.databinding.ObservableField
import android.databinding.ObservableList
import com.example.there.findclips.model.entity.soundcloud.SoundCloudPlaylist
import com.example.there.findclips.model.entity.soundcloud.SoundCloudSystemPlaylist
import com.example.there.findclips.util.list.IdentifiableNamedObservableListItem
import com.example.there.findclips.util.list.ObservableSortedList

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