package com.example.there.findclips.main

import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import com.example.there.findclips.player.PlayerViewState

data class MainActivityView(
        val state: PlayerViewState,
        val onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener,
        val pagerAdapter: FragmentStatePagerAdapter,
        val onPageChangeListener: ViewPager.OnPageChangeListener
)