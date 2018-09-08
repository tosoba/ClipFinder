package com.example.there.findclips.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.ViewGroup
import com.example.there.findclips.fragment.dashboard.DashboardHostFragment
import com.example.there.findclips.fragment.favourites.FavouritesHostFragment
import com.example.there.findclips.fragment.search.SearchHostFragment
import com.example.there.findclips.util.ext.mainActivity
import com.example.there.findclips.view.viewpager.adapter.CurrentFragmentStatePagerAdapter

class MainFragmentPagerAdapter(manager: FragmentManager) : CurrentFragmentStatePagerAdapter(manager) {

    private val fragments: Array<Fragment> = arrayOf(DashboardHostFragment(), FavouritesHostFragment(), SearchHostFragment())

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
        updateCurrentFragmentToolbar()
    }

    private fun updateCurrentFragmentToolbar() = currentFragment?.let { it.mainActivity?.updateToolbarBackNavigation(it) }
}