package com.example.spotifyfavourites

import android.support.design.widget.AppBarLayout
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentStatePagerAdapter

class SpotifyFavouritesMainView(
        val pagerAdapter: FragmentStatePagerAdapter,
        val onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener,
        val offScreenPageLimit: Int,
        val onAppBarOffsetChangedListener: AppBarLayout.OnOffsetChangedListener
)