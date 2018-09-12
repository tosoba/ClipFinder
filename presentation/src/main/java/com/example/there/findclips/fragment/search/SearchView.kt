package com.example.there.findclips.fragment.search

import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentStatePagerAdapter

class SearchView(
        val query: String,
        val pagerAdapter: FragmentStatePagerAdapter,
        val onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener,
        val offScreenPageLimit: Int
)