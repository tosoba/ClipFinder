package com.example.there.findclips.activities.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.example.there.findclips.fragments.dashboard.DashboardFragment
import com.example.there.findclips.fragments.favourites.FavouritesFragment
import com.example.there.findclips.fragments.search.SearchFragment
import com.example.there.findclips.view.viewpager.adapter.CurrentFragmentStatePagerAdapter

class MainFragmentPagerAdapter(manager: FragmentManager) : CurrentFragmentStatePagerAdapter(manager) {
    private val fragments: Array<Fragment> = arrayOf(DashboardFragment(), FavouritesFragment(), SearchFragment())

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size
}