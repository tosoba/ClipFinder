package com.example.spotifyfavourites.spotify

import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.coreandroid.model.spotify.*
import com.google.android.material.tabs.TabLayout

class SpotifyFavouritesFragmentView(
    val pagerAdapter: FragmentStatePagerAdapter,
    val onTabSelectedListener: TabLayout.OnTabSelectedListener,
    val onPageChangeListener: ViewPager.OnPageChangeListener
) {
    val fragmentTabs = arrayOf("Albums", "Artists", "Categories", "Playlists", "Tracks")
}

class SpotifyFavouritesFragmentViewState(
    val albums: ArrayList<Album> = ArrayList(),
    val artists: ArrayList<Artist> = ArrayList(),
    val categories: ArrayList<Category> = ArrayList(),
    val playlists: ArrayList<Playlist> = ArrayList(),
    val tracks: ArrayList<Track> = ArrayList()
)
