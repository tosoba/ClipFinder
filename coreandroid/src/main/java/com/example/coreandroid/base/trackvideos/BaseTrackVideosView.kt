package com.example.coreandroid.base.trackvideos

import android.databinding.ObservableField
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.View

abstract class BaseTrackVideosView<Track>(
        val state: TrackVideosViewState<Track>,
        val pagerAdapter: FragmentStatePagerAdapter,
        val onTabSelectedListener: TabLayout.OnTabSelectedListener,
        val onPageChangeListener: ViewPager.OnPageChangeListener,
        val onFavouriteBtnClickListener: View.OnClickListener,
        val onPlayBtnClickListener: View.OnClickListener
)

class TrackVideosViewState<Track>(
        val track: ObservableField<Track> = ObservableField(),
        val isSavedAsFavourite: ObservableField<Boolean> = ObservableField(false)
)