package com.example.soundcloudtrackvideos

import android.view.View
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.coreandroid.base.trackvideos.BaseTrackVideosView
import com.example.coreandroid.base.trackvideos.TrackVideosViewState
import com.example.coreandroid.model.soundcloud.SoundCloudTrack
import com.google.android.material.tabs.TabLayout


class SoundCloudTrackVideosView(
        state: TrackVideosViewState<SoundCloudTrack>,
        pagerAdapter: FragmentStatePagerAdapter,
        onTabSelectedListener: TabLayout.OnTabSelectedListener,
        onPageChangeListener: ViewPager.OnPageChangeListener,
        onFavouriteBtnClickListener: View.OnClickListener
) : BaseTrackVideosView<SoundCloudTrack>(
        state, pagerAdapter, onTabSelectedListener, onPageChangeListener, onFavouriteBtnClickListener
) {
    val fragmentTabs = arrayOf("Clips", "Similar")
}