package com.example.main.soundcloud

import com.google.android.material.bottomnavigation.BottomNavigationView

//TODO: make one class for SoundCloudMainView and SpotifyMainView (they're literally the same)
class SoundCloudMainView(
    val onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener,
    val pagerAdapter: androidx.fragment.app.FragmentStatePagerAdapter,
    val onPageChangeListener: androidx.viewpager.widget.ViewPager.OnPageChangeListener,
    val offScreenPageLimit: Int
)
