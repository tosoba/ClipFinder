package com.example.there.findclips.bindings

import android.databinding.BindingAdapter
import android.support.design.widget.TabLayout


@BindingAdapter("tabs")
fun bindTabs(tabLayout: TabLayout, tabs: Array<String>) {
    tabs.forEach { tabLayout.addTab(tabLayout.newTab().setText(it)) }
}

@BindingAdapter("onTabSelectedListener")
fun bindOnTabSelectedListener(tabLayout: TabLayout, listener: TabLayout.OnTabSelectedListener) {
    tabLayout.addOnTabSelectedListener(listener)
}