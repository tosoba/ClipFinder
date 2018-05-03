package com.example.there.findclips.dashboard

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.example.there.findclips.entities.Category
import com.example.there.findclips.entities.Playlist
import com.example.there.findclips.entities.TopTrack

data class DashboardViewState(
        val categories: ObservableArrayList<Category> = ObservableArrayList(),
        val featuredPlaylists: ObservableArrayList<Playlist> = ObservableArrayList(),
        val topTracks: ObservableArrayList<TopTrack> = ObservableArrayList(),
        val categoriesLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val featuredPlaylistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val topTracksLoadingInProgress: ObservableField<Boolean> = ObservableField(false)
) {
    fun addCategoriesSorted(newCategories: List<Category>) {
        categories.addAll(newCategories.sortedBy { it.name })
    }

    fun addFeaturedPlaylistsSorted(newPlaylists: List<Playlist>) {
        featuredPlaylists.addAll(newPlaylists.sortedBy { it.name })
    }
}