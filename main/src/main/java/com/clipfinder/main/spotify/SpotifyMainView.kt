package com.clipfinder.main.spotify

import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView

class SpotifyMainView(
    val onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener,
    val pagerAdapter: FragmentStatePagerAdapter,
    val onPageChangeListener: ViewPager.OnPageChangeListener,
    val offScreenPageLimit: Int
)
