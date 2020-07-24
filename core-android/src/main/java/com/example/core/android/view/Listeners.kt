package com.example.core.android.view

import android.view.View
import android.widget.SeekBar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

interface OnTabSelectedListener : TabLayout.OnTabSelectedListener {
    override fun onTabReselected(tab: TabLayout.Tab?) = Unit
    override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
}

@FunctionalInterface
interface OnPageChangeListener : ViewPager.OnPageChangeListener {
    override fun onPageScrollStateChanged(state: Int) = Unit
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit
}

interface OnSeekBarProgressChangeListener : SeekBar.OnSeekBarChangeListener {
    override fun onStartTrackingTouch(p0: SeekBar?) = Unit
    override fun onStopTrackingTouch(p0: SeekBar?) = Unit
}

fun onSeekBarProgressChangeListener(
    callback: (seekBar: SeekBar?, progress: Int, fromUser: Boolean) -> Unit
) = object : OnSeekBarProgressChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        callback(seekBar, progress, fromUser)
    }
}

interface OnNavigationDrawerClosedListerner : DrawerLayout.DrawerListener {
    override fun onDrawerOpened(p0: View) = Unit
    override fun onDrawerSlide(p0: View, p1: Float) = Unit
    override fun onDrawerStateChanged(p0: Int) = Unit
}
