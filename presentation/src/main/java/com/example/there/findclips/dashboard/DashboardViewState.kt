package com.example.there.findclips.dashboard

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.example.there.domain.entities.CategoryEntity
import com.example.there.domain.entities.PlaylistEntity
import com.example.there.domain.entities.TopTrackEntity

data class DashboardViewState(
        val categories: ObservableArrayList<CategoryEntity> = ObservableArrayList(),
        val featuredPlaylists: ObservableArrayList<PlaylistEntity> = ObservableArrayList(),
        val topTracks: ObservableArrayList<TopTrackEntity> = ObservableArrayList(),
        val categoriesLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val featuredPlaylistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val topTracksLoadingInProgress: ObservableField<Boolean> = ObservableField(false)
) {
    fun addCategoriesSorted(newCategories: List<CategoryEntity>) {
        categories.addAll(newCategories.sortedBy { it.name })
    }

    fun addFeaturedPlaylistsSorted(newPlaylists: List<PlaylistEntity>) {
        featuredPlaylists.addAll(newPlaylists.sortedBy { it.name })
    }
}