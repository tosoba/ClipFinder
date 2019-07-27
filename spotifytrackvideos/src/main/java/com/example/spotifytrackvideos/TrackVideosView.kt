package com.example.spotifytrackvideos

import android.view.View
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.coreandroid.base.trackvideos.BaseTrackVideosView
import com.example.coreandroid.base.trackvideos.TrackVideosViewState
import com.example.coreandroid.model.spotify.Track
import com.google.android.material.tabs.TabLayout

class TrackVideosView(
        state: TrackVideosViewState<Track>,
        pagerAdapter: FragmentStatePagerAdapter,
        onTabSelectedListener: TabLayout.OnTabSelectedListener,
        onPageChangeListener: ViewPager.OnPageChangeListener,
        onFavouriteBtnClickListener: View.OnClickListener
) : BaseTrackVideosView<Track>(
        state, pagerAdapter, onTabSelectedListener, onPageChangeListener, onFavouriteBtnClickListener
) {
    val fragmentTabs = arrayOf("Clips", "Info")
}