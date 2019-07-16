package com.example.coreandroid.view.binding

import androidx.databinding.BindingAdapter
import com.example.coreandroid.view.viewpager.LockableViewPager

@BindingAdapter("onPageChangeListener")
fun bindOnPageChangeListener(viewPager: androidx.viewpager.widget.ViewPager, listener: androidx.viewpager.widget.ViewPager.OnPageChangeListener) {
    viewPager.addOnPageChangeListener(listener)
}

@BindingAdapter("swipeLocked")
fun bindSwipeLocked(viewPager: LockableViewPager, swipeLocked: Boolean) {
    viewPager.swipeLocked = swipeLocked
}

@BindingAdapter("offScreenPageLimit")
fun bindOffScreenPageLimit(viewPager: androidx.viewpager.widget.ViewPager, limit: Int) {
    viewPager.offscreenPageLimit = limit
}