package com.example.there.findclips.fragment.favourites

import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentStatePagerAdapter

data class FavouritesView(
        val pagerAdapter: FragmentStatePagerAdapter,
        val onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener
)