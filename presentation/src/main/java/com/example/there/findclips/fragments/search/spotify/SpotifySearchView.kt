package com.example.there.findclips.fragments.search.spotify

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import com.example.there.findclips.model.entities.Album
import com.example.there.findclips.model.entities.Artist
import com.example.there.findclips.model.entities.Playlist
import com.example.there.findclips.model.entities.Track

data class SpotifySearchView(
        val state: SpotifySearchViewState,
        val pagerAdapter: FragmentStatePagerAdapter,
        val onTabSelectedListener: TabLayout.OnTabSelectedListener,
        val onPageChangeListener: ViewPager.OnPageChangeListener
) {
    val fragmentTabs = arrayOf("Albums", "Artists", "Playlists", "Tracks")
}

data class SpotifySearchViewState(
        val albums: ObservableArrayList<Album> = ObservableArrayList(),
        val artists: ObservableArrayList<Artist> = ObservableArrayList(),
        val playlists: ObservableArrayList<Playlist> = ObservableArrayList(),
        val tracks: ObservableArrayList<Track> = ObservableArrayList(),
        val loadingInProgress: ObservableField<Boolean> = ObservableField(false)
) {
    fun addAlbumsSorted(newAlbums: List<Album>) {
        albums.addAll(newAlbums.sortedBy { it.name })
    }

    fun addArtistsSorted(newArtists: List<Artist>) {
        artists.addAll(newArtists.sortedBy { it.name })
    }

    fun addPlaylistsSorted(newPlaylists: List<Playlist>) {
        playlists.addAll(newPlaylists.sortedBy { it.name })
    }

    fun addTracksSorted(newTracks: List<Track>) {
        tracks.addAll(newTracks.sortedBy { it.name })
    }

    fun clearAll() {
        albums.clear()
        artists.clear()
        playlists.clear()
        tracks.clear()
    }
}