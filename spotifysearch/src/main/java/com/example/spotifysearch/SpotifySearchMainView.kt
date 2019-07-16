package com.example.spotifysearch

import com.google.android.material.bottomnavigation.BottomNavigationView

class SpotifySearchMainView(
        val query: String,
        val pagerAdapter: androidx.fragment.app.FragmentStatePagerAdapter,
        val onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener,
        val offScreenPageLimit: Int
)