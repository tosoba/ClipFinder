package com.example.there.findclips.fragment.trackvideos

import android.databinding.ObservableField
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import com.example.there.findclips.model.entity.Track

class TrackVideosView(
        val state: TrackVideosViewState,
        val pagerAdapter: FragmentStatePagerAdapter,
        val onTabSelectedListener: TabLayout.OnTabSelectedListener,
        val onPageChangeListener: ViewPager.OnPageChangeListener,
        val onFavouriteBtnClickListener: View.OnClickListener,
        val onPlayBtnClickListener: View.OnClickListener
) {
    val fragmentTabs = arrayOf("Clips", "Info")
}

data class TrackVideosViewState(
        val track: ObservableField<Track> = ObservableField(),
        val isSavedAsFavourite: ObservableField<Boolean> = ObservableField(false)
)