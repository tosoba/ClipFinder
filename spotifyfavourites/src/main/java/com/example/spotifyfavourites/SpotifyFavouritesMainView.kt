package com.example.spotifyfavourites

import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

class SpotifyFavouritesMainView(
        val pagerAdapter: androidx.fragment.app.FragmentStatePagerAdapter,
        val onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener,
        val offScreenPageLimit: Int,
        val onAppBarOffsetChangedListener: AppBarLayout.OnOffsetChangedListener
)