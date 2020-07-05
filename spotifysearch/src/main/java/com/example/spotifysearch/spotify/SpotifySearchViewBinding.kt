package com.example.spotifysearch.spotify

import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class SpotifySearchViewBinding(
    val pagerAdapter: PagerAdapter,
    val onTabSelectedListener: TabLayout.OnTabSelectedListener,
    val onPageChangeListener: ViewPager.OnPageChangeListener
) {
    val fragmentTabs = arrayOf("Albums", "Artists", "Playlists", "Tracks")
}
