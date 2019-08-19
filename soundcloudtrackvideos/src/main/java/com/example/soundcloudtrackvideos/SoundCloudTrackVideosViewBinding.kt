package com.example.soundcloudtrackvideos

import android.view.View
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.ViewPager
import com.example.coreandroid.base.trackvideos.BaseTrackVideosViewBinding
import com.example.coreandroid.model.soundcloud.SoundCloudTrack
import com.google.android.material.tabs.TabLayout


class SoundCloudTrackVideosViewBinding(
        track: MutableLiveData<SoundCloudTrack>,
        pagerAdapter: FragmentStatePagerAdapter,
        onTabSelectedListener: TabLayout.OnTabSelectedListener,
        onPageChangeListener: ViewPager.OnPageChangeListener,
        onFavouriteBtnClickListener: View.OnClickListener
) : BaseTrackVideosViewBinding<SoundCloudTrack>(
        track, pagerAdapter, onTabSelectedListener, onPageChangeListener, onFavouriteBtnClickListener
) {
    val fragmentTabs = arrayOf("Clips", "Similar")
}