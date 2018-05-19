package com.example.there.findclips.activities.trackvideos

import android.databinding.ObservableField
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import com.example.there.findclips.model.entities.Track
import com.example.there.findclips.activities.player.PlayerViewState

data class TrackVideosView(
        val state: TrackVideosViewState,
        val pagerAdapter: FragmentStatePagerAdapter,
        val onTabSelectedListener: TabLayout.OnTabSelectedListener,
        val onPageChangeListener: ViewPager.OnPageChangeListener,
        val onFavouriteBtnClickListener: View.OnClickListener
) {
    val fragmentTabs = arrayOf("Clips", "Info")
}

class TrackVideosViewState(videoIsOpen: ObservableField<Boolean> = ObservableField(false),
                           isMaximized: ObservableField<Boolean> = ObservableField(true),
                           val track: ObservableField<Track> = ObservableField()) : PlayerViewState(videoIsOpen, isMaximized)