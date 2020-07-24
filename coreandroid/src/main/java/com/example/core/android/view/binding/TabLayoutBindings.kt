package com.example.core.android.view.binding

import androidx.databinding.BindingAdapter
import com.google.android.material.tabs.TabLayout

@BindingAdapter("tabs")
fun bindTabs(tabLayout: TabLayout, tabs: Array<String>) {
    tabs.forEach { tabLayout.addTab(tabLayout.newTab().setText(it)) }
}

@BindingAdapter("onTabSelectedListener")
fun bindOnTabSelectedListener(tabLayout: TabLayout, listener: TabLayout.OnTabSelectedListener) {
    tabLayout.addOnTabSelectedListener(listener)
}
