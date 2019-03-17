package com.example.there.findclips.view.viewpager.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager


open class CustomCurrentStatePagerAdapter(
        fragmentManager: FragmentManager,
        private val fragments: Array<Fragment>
) : CurrentFragmentStatePagerAdapter(fragmentManager) {

    val currentNavHostFragment: com.example.coreandroid.base.fragment.BaseNavHostFragment?
        get() = currentFragment as? com.example.coreandroid.base.fragment.BaseNavHostFragment

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size
}