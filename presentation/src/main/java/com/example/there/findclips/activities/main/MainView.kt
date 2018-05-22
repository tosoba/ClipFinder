package com.example.there.findclips.activities.main

import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager

data class MainView(
        val onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener,
        val pagerAdapter: FragmentStatePagerAdapter,
        val onPageChangeListener: ViewPager.OnPageChangeListener
)