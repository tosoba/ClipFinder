package com.example.there.findclips.bindings

import android.databinding.BindingAdapter
import android.support.v4.view.ViewPager


@BindingAdapter("onPageChangeListener")
fun bindOnPageChangeListener(viewPager: ViewPager, listener: ViewPager.OnPageChangeListener) {
    viewPager.addOnPageChangeListener(listener)
}