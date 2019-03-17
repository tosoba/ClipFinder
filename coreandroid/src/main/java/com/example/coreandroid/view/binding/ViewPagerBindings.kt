package com.example.coreandroid.view.binding

import android.databinding.BindingAdapter
import android.support.v4.view.ViewPager
import com.example.there.findclips.view.viewpager.LockableViewPager

@BindingAdapter("onPageChangeListener")
fun bindOnPageChangeListener(viewPager: ViewPager, listener: ViewPager.OnPageChangeListener) {
    viewPager.addOnPageChangeListener(listener)
}

@BindingAdapter("swipeLocked")
fun bindSwipeLocked(viewPager: LockableViewPager, swipeLocked: Boolean) {
    viewPager.swipeLocked = swipeLocked
}

@BindingAdapter("offScreenPageLimit")
fun bindOffScreenPageLimit(viewPager: ViewPager, limit: Int) {
    viewPager.offscreenPageLimit = limit
}