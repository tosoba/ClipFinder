package com.example.there.findclips.view

import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.widget.SeekBar

interface OnTabSelectedListener : TabLayout.OnTabSelectedListener {
    override fun onTabReselected(tab: TabLayout.Tab?) = Unit
    override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
}

interface OnPageChangeListener : ViewPager.OnPageChangeListener {
    override fun onPageScrollStateChanged(state: Int) = Unit
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit
}

interface OnSeekBarProgressChangeListener : SeekBar.OnSeekBarChangeListener {
    override fun onStartTrackingTouch(p0: SeekBar?) = Unit
    override fun onStopTrackingTouch(p0: SeekBar?) = Unit
}