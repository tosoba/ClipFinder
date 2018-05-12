package com.example.there.findclips.trackvideos

import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import com.example.there.findclips.entities.Track
import com.example.there.findclips.player.PlayerViewState

data class TrackVideosView(
        val track: Track,
        val state: PlayerViewState,
        val pagerAdapter: FragmentStatePagerAdapter,
        val onTabSelectedListener: TabLayout.OnTabSelectedListener,
        val onPageChangeListener: ViewPager.OnPageChangeListener
) {
    val fragmentTabs = arrayOf("Clips", "Info")
}