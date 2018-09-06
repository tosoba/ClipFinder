package com.example.there.findclips.fragments.dashboard

import android.databinding.ObservableField
import android.databinding.ObservableList
import com.example.there.findclips.model.entities.Category
import com.example.there.findclips.model.entities.Playlist
import com.example.there.findclips.model.entities.TopTrack
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.view.lists.CategoriesList
import com.example.there.findclips.view.lists.PlaylistsList
import com.example.there.findclips.view.lists.TopTracksList

data class DashboardView(
        val state: DashboardViewState,
        val categoriesAdapter: CategoriesList.Adapter,
        val playlistsAdapter: PlaylistsList.Adapter,
        val topTracksAdapter: TopTracksList.Adapter
)

data class DashboardViewState(
        val categories: ObservableList<Category> = ObservableSortedList<Category>(Category::class.java, object : ObservableSortedList.Callback<Category> {
            override fun compare(o1: Category, o2: Category): Int = o1.name.toLowerCase().compareTo(o2.name.toLowerCase())

            override fun areItemsTheSame(item1: Category, item2: Category): Boolean = item1.id == item2.id

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean = oldItem.id == newItem.id
        }),
        val featuredPlaylists: ObservableList<Playlist> = ObservableSortedList<Playlist>(Playlist::class.java, object : ObservableSortedList.Callback<Playlist> {
            override fun compare(o1: Playlist, o2: Playlist): Int = o1.name.toLowerCase().compareTo(o2.name.toLowerCase())

            override fun areItemsTheSame(item1: Playlist, item2: Playlist): Boolean = item1.id == item2.id

            override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean = oldItem.id == newItem.id
        }),
        val topTracks: ObservableList<TopTrack> = ObservableSortedList<TopTrack>(TopTrack::class.java, object : ObservableSortedList.Callback<TopTrack> {
            override fun compare(o1: TopTrack, o2: TopTrack): Int = o1.position.compareTo(o2.position)

            override fun areItemsTheSame(item1: TopTrack, item2: TopTrack): Boolean = item1.track.id == item2.track.id

            override fun areContentsTheSame(oldItem: TopTrack, newItem: TopTrack): Boolean = oldItem.track.id == newItem.track.id
        }),
        val categoriesLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val featuredPlaylistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val topTracksLoadingInProgress: ObservableField<Boolean> = ObservableField(false)
)