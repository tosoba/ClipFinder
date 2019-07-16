package com.example.coreandroid.base.trackvideos

import android.view.View
import androidx.databinding.ObservableField
import com.google.android.material.tabs.TabLayout

abstract class BaseTrackVideosView<Track>(
        val state: TrackVideosViewState<Track>,
        val pagerAdapter: androidx.fragment.app.FragmentStatePagerAdapter,
        val onTabSelectedListener: TabLayout.OnTabSelectedListener,
        val onPageChangeListener: androidx.viewpager.widget.ViewPager.OnPageChangeListener,
        val onFavouriteBtnClickListener: View.OnClickListener,
        val onPlayBtnClickListener: View.OnClickListener
)

class TrackVideosViewState<Track>(
        val track: ObservableField<Track> = ObservableField(),
        val isSavedAsFavourite: ObservableField<Boolean> = ObservableField(false)
)