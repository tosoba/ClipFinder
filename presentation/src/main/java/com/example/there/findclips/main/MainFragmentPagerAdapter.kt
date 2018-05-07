package com.example.there.findclips.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.there.findclips.dashboard.DashboardFragment
import com.example.there.findclips.favourites.FavouritesFragment
import com.example.there.findclips.search.SearchFragment

class MainFragmentPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
    private val fragments: Array<Fragment> = arrayOf(DashboardFragment(), FavouritesFragment(), SearchFragment())

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size
}