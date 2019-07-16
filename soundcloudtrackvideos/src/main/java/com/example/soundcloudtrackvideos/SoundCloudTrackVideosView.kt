package com.example.soundcloudtrackvideos

import android.view.View
import com.example.coreandroid.base.trackvideos.BaseTrackVideosView
import com.example.coreandroid.base.trackvideos.TrackVideosViewState
import com.example.coreandroid.model.soundcloud.SoundCloudTrack
import com.google.android.material.tabs.TabLayout


class SoundCloudTrackVideosView(
        state: TrackVideosViewState<SoundCloudTrack>,
        pagerAdapter: androidx.fragment.app.FragmentStatePagerAdapter,
        onTabSelectedListener: TabLayout.OnTabSelectedListener,
        onPageChangeListener: androidx.viewpager.widget.ViewPager.OnPageChangeListener,
        onFavouriteBtnClickListener: View.OnClickListener,
        onPlayBtnClickListener: View.OnClickListener
) : BaseTrackVideosView<SoundCloudTrack>(
        state, pagerAdapter, onTabSelectedListener, onPageChangeListener, onFavouriteBtnClickListener, onPlayBtnClickListener
) {
    val fragmentTabs = arrayOf("Clips", "Similar")
}