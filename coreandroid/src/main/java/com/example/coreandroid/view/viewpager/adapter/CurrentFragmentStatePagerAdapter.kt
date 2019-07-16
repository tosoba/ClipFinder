package com.example.coreandroid.view.viewpager.adapter

import android.view.ViewGroup

abstract class CurrentFragmentStatePagerAdapter(
        manager: androidx.fragment.app.FragmentManager
) : androidx.fragment.app.FragmentStatePagerAdapter(manager) {

    var currentFragment: androidx.fragment.app.Fragment? = null
        private set

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        if (currentFragment !== `object`) {
            currentFragment = `object` as androidx.fragment.app.Fragment
        }
        super.setPrimaryItem(container, position, `object`)
    }
}