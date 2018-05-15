package com.example.there.findclips.trackvideos

import android.databinding.ObservableField
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import com.example.there.findclips.entities.Track
import com.example.there.findclips.player.PlayerViewState

data class TrackVideosView(
        val state: TrackVideosViewState,
        val pagerAdapter: FragmentStatePagerAdapter,
        val onTabSelectedListener: TabLayout.OnTabSelectedListener,
        val onPageChangeListener: ViewPager.OnPageChangeListener
) {
    val fragmentTabs = arrayOf("Clips", "Info")
}

class TrackVideosViewState(videoIsOpen: ObservableField<Boolean> = ObservableField(false),
                           isMaximized: ObservableField<Boolean> = ObservableField(true),
                           val track: ObservableField<Track> = ObservableField()) : PlayerViewState(videoIsOpen, isMaximized)