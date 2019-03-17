package com.example.there.findclips.spotify.favourites.spotify

import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager

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