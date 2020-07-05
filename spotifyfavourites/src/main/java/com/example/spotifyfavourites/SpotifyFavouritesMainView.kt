package com.example.spotifyfavourites

import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

class SpotifyFavouritesMainView(
    val pagerAdapter: FragmentStatePagerAdapter,
    val onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener,
    val offScreenPageLimit: Int,
    val onAppBarOffsetChangedListener: AppBarLayout.OnOffsetChangedListener
)
