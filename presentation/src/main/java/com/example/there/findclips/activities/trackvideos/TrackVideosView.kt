package com.example.there.findclips.activities.trackvideos

import android.databinding.ObservableField
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import com.example.there.findclips.view.player.PlayerViewState
import com.example.there.findclips.model.entities.Track

data class TrackVideosView(
        val state: TrackVideosViewState,
        val playerState: PlayerViewState,
        val pagerAdapter: FragmentStatePagerAdapter,
        val onTabSelectedListener: TabLayout.OnTabSelectedListener,
        val onPageChangeListener: ViewPager.OnPageChangeListener,
        val onFavouriteBtnClickListener: View.OnClickListener
) {
    val fragmentTabs = arrayOf("Clips", "Info")
}

data class TrackVideosViewState(val track: ObservableField<Track> = ObservableField())