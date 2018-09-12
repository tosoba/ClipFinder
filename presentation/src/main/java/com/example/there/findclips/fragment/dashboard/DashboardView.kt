package com.example.there.findclips.fragment.dashboard

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.example.there.findclips.model.entity.Album
import com.example.there.findclips.model.entity.Category
import com.example.there.findclips.model.entity.Playlist
import com.example.there.findclips.model.entity.TopTrack
import com.example.there.findclips.util.ObservableSortedList

class DashboardView(
        val state: DashboardViewState,
        val dashboardAdapter: DashboardAdapter
)

data class DashboardViewState(
        val categories: ObservableList<Category> = ObservableSortedList<Category>(Category::class.java, Category.sortedListCallback),
        val featuredPlaylists: ObservableList<Playlist> = ObservableSortedList<Playlist>(Playlist::class.java, Playlist.sortedListCallback),
        val topTracks: ObservableList<TopTrack> = ObservableSortedList<TopTrack>(TopTrack::class.java, TopTrack.sortedListCallback),
        val newReleases: ObservableList<Album> = ObservableArrayList(),
        val categoriesLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val featuredPlaylistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val topTracksLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val newReleasesLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val categoriesErrorOccurred: ObservableField<Boolean> = ObservableField(false),
        val playlistsErrorOccurred: ObservableField<Boolean> = ObservableField(false),
        val topTracksErrorOccurred: ObservableField<Boolean> = ObservableField(false),
        val newReleasesErrorOccurred: ObservableField<Boolean> = ObservableField(false)
)