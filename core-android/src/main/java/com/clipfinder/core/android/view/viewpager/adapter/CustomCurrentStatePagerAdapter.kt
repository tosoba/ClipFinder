package com.clipfinder.core.android.view.viewpager.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

open class CustomCurrentStatePagerAdapter(
    fragmentManager: FragmentManager,
    private val fragments: Array<Fragment>
) : CurrentFragmentStatePagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment = fragments[position]
    override fun getCount(): Int = fragments.size
}
