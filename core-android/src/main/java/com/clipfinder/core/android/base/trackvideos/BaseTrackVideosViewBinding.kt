package com.clipfinder.core.android.base.trackvideos

import android.view.View
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.ViewPager
import com.airbnb.mvrx.MvRxState
import com.google.android.material.tabs.TabLayout

class TrackVideosViewBinding<Track>(
    val fragmentTabs: Array<String>,
    val track: MutableLiveData<Track>,
    val pagerAdapter: FragmentStatePagerAdapter,
    val onTabSelectedListener: TabLayout.OnTabSelectedListener,
    val onPageChangeListener: ViewPager.OnPageChangeListener,
    val onFavouriteBtnClickListener: View.OnClickListener
)

data class TrackVideosViewState<Track>(val tracks: List<Track>) : MvRxState {
    constructor(argTrack: Track) : this(listOf(argTrack))
}
