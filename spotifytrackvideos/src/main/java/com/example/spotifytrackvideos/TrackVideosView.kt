package com.example.spotifytrackvideos

import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import com.example.coreandroid.base.trackvideos.BaseTrackVideosView
import com.example.coreandroid.base.trackvideos.TrackVideosViewState
import com.example.coreandroid.model.spotify.Track

class TrackVideosView(
        state: TrackVideosViewState<Track>,
        pagerAdapter: FragmentStatePagerAdapter,
        onTabSelectedListener: TabLayout.OnTabSelectedListener,
        onPageChangeListener: ViewPager.OnPageChangeListener,
        onFavouriteBtnClickListener: View.OnClickListener,
        onPlayBtnClickListener: View.OnClickListener
) : BaseTrackVideosView<Track>(
        state, pagerAdapter, onTabSelectedListener, onPageChangeListener, onFavouriteBtnClickListener, onPlayBtnClickListener
) {
    val fragmentTabs = arrayOf("Clips", "Info")
}