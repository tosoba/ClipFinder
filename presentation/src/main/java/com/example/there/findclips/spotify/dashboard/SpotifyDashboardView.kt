package com.example.there.findclips.spotify.dashboard

import android.databinding.ObservableField
import android.databinding.ObservableList
import com.example.there.findclips.model.entity.spotify.Album
import com.example.there.findclips.model.entity.spotify.Category
import com.example.there.findclips.model.entity.spotify.Playlist
import com.example.there.findclips.model.entity.spotify.TopTrack
import com.example.there.findclips.util.ObservableSortedList

class SpotifyDashboardView(
        val state: SpotifyDashboardViewState,
        val dashboardAdapter: SpotifyDashboardAdapter
)

class SpotifyDashboardViewState(
        val categories: ObservableList<Category> = ObservableSortedList<Category>(Category::class.java, Category.sortedListCallback),
        val featuredPlaylists: ObservableList<Playlist> = ObservableSortedList<Playlist>(Playlist::class.java, Playlist.sortedListCallback),
        val topTracks: ObservableList<TopTrack> = ObservableSortedList<TopTrack>(TopTrack::class.java, TopTrack.sortedListCallback),
        val newReleases: ObservableList<Album> = ObservableSortedList<Album>(Album::class.java, Album.unsortedListCallback),
        val categoriesLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val featuredPlaylistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val topTracksLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val newReleasesLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val categoriesLoadingErrorOccurred: ObservableField<Boolean> = ObservableField(false),
        val featuredPlaylistsLoadingErrorOccurred: ObservableField<Boolean> = ObservableField(false),
        val topTracksLoadingErrorOccurred: ObservableField<Boolean> = ObservableField(false),
        val newReleasesLoadingErrorOccurred: ObservableField<Boolean> = ObservableField(false)
)