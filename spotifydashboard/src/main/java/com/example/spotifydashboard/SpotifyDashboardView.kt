package com.example.spotifydashboard

import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.Category
import com.example.coreandroid.model.spotify.Playlist
import com.example.coreandroid.model.spotify.TopTrack
import com.example.coreandroid.util.list.IdentifiableNamedObservableListItem
import com.example.coreandroid.util.list.IdentifiableNumberedObservableListItem
import com.example.coreandroid.util.list.IdentifiableObservableListItem
import com.example.coreandroid.util.list.ObservableSortedList

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