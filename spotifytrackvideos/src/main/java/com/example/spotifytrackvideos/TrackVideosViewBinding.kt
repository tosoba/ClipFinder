package com.example.spotifytrackvideos

import android.view.View
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.ViewPager
import com.example.coreandroid.base.trackvideos.BaseTrackVideosViewBinding
import com.example.coreandroid.model.spotify.Track
import com.google.android.material.tabs.TabLayout

//TODO: what is the point of inheritance here lol? - get rid of this
//maybe also replace inheritance in track videos viewModels with composition
class TrackVideosViewBinding(
        track: MutableLiveData<Track>,
        pagerAdapter: FragmentStatePagerAdapter,
        onTabSelectedListener: TabLayout.OnTabSelectedListener,
        onPageChangeListener: ViewPager.OnPageChangeListener,
        onFavouriteBtnClickListener: View.OnClickListener
) : BaseTrackVideosViewBinding<Track>(
        track, pagerAdapter, onTabSelectedListener, onPageChangeListener, onFavouriteBtnClickListener
) {
    val fragmentTabs = arrayOf("Clips", "Info")
}