package com.example.spotifytrackvideos

import android.view.View
import com.example.coreandroid.base.trackvideos.BaseTrackVideosView
import com.example.coreandroid.base.trackvideos.TrackVideosViewState
import com.example.coreandroid.model.spotify.Track
import com.google.android.material.tabs.TabLayout

class TrackVideosView(
        state: TrackVideosViewState<Track>,
        pagerAdapter: androidx.fragment.app.FragmentStatePagerAdapter,
        onTabSelectedListener: TabLayout.OnTabSelectedListener,
        onPageChangeListener: androidx.viewpager.widget.ViewPager.OnPageChangeListener,
        onFavouriteBtnClickListener: View.OnClickListener,
        onPlayBtnClickListener: View.OnClickListener
) : BaseTrackVideosView<Track>(
        state, pagerAdapter, onTabSelectedListener, onPageChangeListener, onFavouriteBtnClickListener, onPlayBtnClickListener
) {
    val fragmentTabs = arrayOf("Clips", "Info")
}