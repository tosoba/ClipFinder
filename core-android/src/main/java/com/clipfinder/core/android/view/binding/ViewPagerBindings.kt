package com.clipfinder.core.android.view.binding

import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import com.clipfinder.core.android.view.viewpager.LockableViewPager

@BindingAdapter("onPageChangeListener")
fun bindOnPageChangeListener(viewPager: ViewPager, listener: ViewPager.OnPageChangeListener) {
    viewPager.addOnPageChangeListener(listener)
}

@BindingAdapter("swipeLocked")
fun bindSwipeLocked(viewPager: _root_ide_package_.com.clipfinder.core.android.view.viewpager.LockableViewPager, swipeLocked: Boolean) {
    viewPager.swipeLocked = swipeLocked
}

@BindingAdapter("offScreenPageLimit")
fun bindOffScreenPageLimit(viewPager: ViewPager, limit: Int) {
    viewPager.offscreenPageLimit = limit
}
