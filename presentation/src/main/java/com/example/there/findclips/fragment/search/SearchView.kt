package com.example.there.findclips.fragment.search

import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentStatePagerAdapter

data class SearchView(
        val pagerAdapter: FragmentStatePagerAdapter,
        val onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener
)