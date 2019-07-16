package com.example.coreandroid.view.viewpager.adapter

import com.example.coreandroid.base.fragment.BaseNavHostFragment


open class CustomCurrentStatePagerAdapter(
        fragmentManager: androidx.fragment.app.FragmentManager,
        private val fragments: Array<androidx.fragment.app.Fragment>
) : CurrentFragmentStatePagerAdapter(fragmentManager) {

    val currentNavHostFragment: BaseNavHostFragment?
        get() = currentFragment as? BaseNavHostFragment

    override fun getItem(position: Int): androidx.fragment.app.Fragment = fragments[position]

    override fun getCount(): Int = fragments.size
}