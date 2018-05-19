package com.example.there.findclips.activities.main

import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import com.example.there.findclips.view.player.PlayerViewState

data class MainView(
        val state: PlayerViewState,
        val onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener,
        val pagerAdapter: FragmentStatePagerAdapter,
        val onPageChangeListener: ViewPager.OnPageChangeListener
)