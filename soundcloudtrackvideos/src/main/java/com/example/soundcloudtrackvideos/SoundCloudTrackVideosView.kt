package com.example.soundcloudtrackvideos

import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import com.example.coreandroid.base.trackvideos.BaseTrackVideosView
import com.example.coreandroid.base.trackvideos.TrackVideosViewState
import com.example.coreandroid.model.soundcloud.SoundCloudTrack


class SoundCloudTrackVideosView(
        state: TrackVideosViewState<SoundCloudTrack>,
        pagerAdapter: FragmentStatePagerAdapter,
        onTabSelectedListener: TabLayout.OnTabSelectedListener,
        onPageChangeListener: ViewPager.OnPageChangeListener,
        onFavouriteBtnClickListener: View.OnClickListener,
        onPlayBtnClickListener: View.OnClickListener
) : BaseTrackVideosView<SoundCloudTrack>(
        state, pagerAdapter, onTabSelectedListener, onPageChangeListener, onFavouriteBtnClickListener, onPlayBtnClickListener
) {
    val fragmentTabs = arrayOf("Clips", "Similar")
}