package com.example.there.findclips.fragment.favourites

import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentStatePagerAdapter

class FavouritesView(
        val pagerAdapter: FragmentStatePagerAdapter,
        val onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener,
        val offScreenPageLimit: Int
)