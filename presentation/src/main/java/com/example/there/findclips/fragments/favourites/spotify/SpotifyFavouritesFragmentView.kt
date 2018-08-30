package com.example.there.findclips.fragments.favourites.spotify

import android.databinding.ObservableArrayList
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import com.example.there.findclips.model.entities.*

data class SpotifyFavouritesFragmentView(
        val pagerAdapter: FragmentStatePagerAdapter,
        val onTabSelectedListener: TabLayout.OnTabSelectedListener,
        val onPageChangeListener: ViewPager.OnPageChangeListener
) {
    val fragmentTabs = arrayOf("Albums", "Artists", "Categories", "Playlists", "Tracks")
}

data class SpotifyFavouritesFragmentViewState(
        val albums: ObservableArrayList<Album> = ObservableArrayList(),
        val artists: ObservableArrayList<Artist> = ObservableArrayList(),
        val categories: ObservableArrayList<Category> = ObservableArrayList(),
        val playlists: ObservableArrayList<Playlist> = ObservableArrayList(),
        val tracks: ObservableArrayList<Track> = ObservableArrayList()
) {
    fun clearAll() {
        albums.clear()
        artists.clear()
        categories.clear()
        playlists.clear()
        tracks.clear()
    }
}