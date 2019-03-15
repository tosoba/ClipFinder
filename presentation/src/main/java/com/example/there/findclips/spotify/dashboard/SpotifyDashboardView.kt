package com.example.there.findclips.spotify.dashboard

import android.databinding.ObservableField
import android.databinding.ObservableList
import com.example.there.findclips.model.entity.spotify.Album
import com.example.there.findclips.model.entity.spotify.Category
import com.example.there.findclips.model.entity.spotify.Playlist
import com.example.there.findclips.model.entity.spotify.TopTrack
import com.example.there.findclips.util.list.IdentifiableNamedObservableListItem
import com.example.there.findclips.util.list.IdentifiableNumberedObservableListItem
import com.example.there.findclips.util.list.IdentifiableObservableListItem
import com.example.there.findclips.util.list.ObservableSortedList

class SpotifyDashboardView(
        val state: SpotifyDashboardViewState,
        val dashboardAdapter: SpotifyDashboardAdapter
)

class SpotifyDashboardViewState(
        val categories: ObservableList<Category> = ObservableSortedList<Category>(Category::class.java, IdentifiableNamedObservableListItem.sortedByNameCallback()),
        val featuredPlaylists: ObservableList<Playlist> = ObservableSortedList<Playlist>(Playlist::class.java, IdentifiableNamedObservableListItem.sortedByNameCallback()),
        val topTracks: ObservableList<TopTrack> = ObservableSortedList<TopTrack>(TopTrack::class.java, IdentifiableNumberedObservableListItem.sortedByNumberCallback()),
        val newReleases: ObservableList<Album> = ObservableSortedList<Album>(Album::class.java, IdentifiableObservableListItem.unsortedCallback()),
        val categoriesLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val featuredPlaylistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val topTracksLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val newReleasesLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val categoriesLoadingErrorOccurred: ObservableField<Boolean> = ObservableField(false),
        val featuredPlaylistsLoadingErrorOccurred: ObservableField<Boolean> = ObservableField(false),
        val topTracksLoadingErrorOccurred: ObservableField<Boolean> = ObservableField(false),
        val newReleasesLoadingErrorOccurred: ObservableField<Boolean> = ObservableField(false)
)