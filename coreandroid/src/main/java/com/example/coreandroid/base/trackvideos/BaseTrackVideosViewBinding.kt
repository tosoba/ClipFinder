package com.example.coreandroid.base.trackvideos

import android.view.View
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.ViewPager
import com.airbnb.mvrx.MvRxState
import com.example.coreandroid.model.Data
import com.example.coreandroid.model.DataList
import com.google.android.material.tabs.TabLayout

class TrackVideosViewBinding<Track>(
        val fragmentTabs: Array<String>,
        val track: MutableLiveData<Track>,
        val pagerAdapter: FragmentStatePagerAdapter,
        val onTabSelectedListener: TabLayout.OnTabSelectedListener,
        val onPageChangeListener: ViewPager.OnPageChangeListener,
        val onFavouriteBtnClickListener: View.OnClickListener
)

data class TrackVideosViewState<Track>(
        val tracks: DataList<Track> = DataList(),
        val isSavedAsFavourite: Data<Boolean> = Data(false)
) : MvRxState {
    constructor(argTrack: Track) : this(DataList(listOf(argTrack)))
}
