package com.example.main.spotify

import com.google.android.material.bottomnavigation.BottomNavigationView

class SpotifyMainView(
        val onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener,
        val pagerAdapter: androidx.fragment.app.FragmentStatePagerAdapter,
        val onPageChangeListener: androidx.viewpager.widget.ViewPager.OnPageChangeListener,
        val offScreenPageLimit: Int
)