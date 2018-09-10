package com.example.there.findclips.fragment.dashboard

import android.databinding.ObservableField
import android.databinding.ObservableList
import com.example.there.findclips.model.entity.Category
import com.example.there.findclips.model.entity.Playlist
import com.example.there.findclips.model.entity.TopTrack
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.view.list.impl.CategoriesList
import com.example.there.findclips.view.list.impl.PlaylistsList
import com.example.there.findclips.view.list.impl.TopTracksList

data class DashboardView(
        val state: DashboardViewState,
        val categoriesAdapter: CategoriesList.Adapter,
        val playlistsAdapter: PlaylistsList.Adapter,
        val topTracksAdapter: TopTracksList.Adapter
)

data class DashboardViewState(
        val categories: ObservableList<Category> = ObservableSortedList<Category>(Category::class.java, Category.sortedListCallback),
        val featuredPlaylists: ObservableList<Playlist> = ObservableSortedList<Playlist>(Playlist::class.java, Playlist.sortedListCallback),
        val topTracks: ObservableList<TopTrack> = ObservableSortedList<TopTrack>(TopTrack::class.java, TopTrack.sortedListCallback),
        val categoriesLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val featuredPlaylistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val topTracksLoadingInProgress: ObservableField<Boolean> = ObservableField(false)
)