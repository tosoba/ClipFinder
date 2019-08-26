package com.example.spotifysearch

import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView


class SpotifySearchMainView(
        val query: String,
        val pagerAdapter: PagerAdapter,
        val onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener,
        val offScreenPageLimit: Int
)