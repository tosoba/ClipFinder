package com.example.coreandroid.view.viewpager.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.example.coreandroid.base.fragment.BaseNavHostFragment


open class CustomCurrentStatePagerAdapter(
        fragmentManager: FragmentManager,
        private val fragments: Array<Fragment>
) : CurrentFragmentStatePagerAdapter(fragmentManager) {

    val currentNavHostFragment: BaseNavHostFragment?
        get() = currentFragment as? BaseNavHostFragment

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size
}