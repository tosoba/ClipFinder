package com.example.spotifysearch.spotify

import android.databinding.ObservableField
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.Artist
import com.example.coreandroid.model.spotify.Playlist
import com.example.coreandroid.model.spotify.Track

class SpotifySearchView(
        val state: SpotifySearchViewState,
        val pagerAdapter: FragmentStatePagerAdapter,
        val onTabSelectedListener: TabLayout.OnTabSelectedListener,
        val onPageChangeListener: ViewPager.OnPageChangeListener
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