package com.example.spotifysearch.spotify

import androidx.databinding.ObservableField
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.Artist
import com.example.coreandroid.model.spotify.Playlist
import com.example.coreandroid.model.spotify.Track
import com.google.android.material.tabs.TabLayout

class SpotifySearchView(
        val state: SpotifySearchViewState,
        val pagerAdapter: androidx.fragment.app.FragmentStatePagerAdapter,
        val onTabSelectedListener: TabLayout.OnTabSelectedListener,
        val onPageChangeListener: androidx.viewpager.widget.ViewPager.OnPageChangeListener
) {
    val fragmentTabs = arrayOf("Albums", "Artists", "Playlists", "Tracks")
}

class SpotifySearchViewState(
        val albums: ArrayList<Album> = ArrayList(),
        val artists: ArrayList<Artist> = ArrayList(),
        val playlists: ArrayList<Playlist> = ArrayList(),
        val tracks: ArrayList<Track> = ArrayList(),
        val loadingInProgress: ObservableField<Boolean> = ObservableField(false)
) {
    fun clearAll() {
        albums.clear()
        artists.clear()
        playlists.clear()
        tracks.clear()
    }
}