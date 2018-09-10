package com.example.there.findclips.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.example.there.findclips.base.fragment.BaseHostFragment
import com.example.there.findclips.fragment.dashboard.DashboardHostFragment
import com.example.there.findclips.fragment.favourites.FavouritesHostFragment
import com.example.there.findclips.fragment.user.UserHostFragment
import com.example.there.findclips.view.viewpager.adapter.CurrentFragmentStatePagerAdapter

class MainFragmentPagerAdapter(manager: FragmentManager) : CurrentFragmentStatePagerAdapter(manager) {

    private val fragments: Array<Fragment> = arrayOf(DashboardHostFragment(), UserHostFragment(), FavouritesHostFragment())

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    val currentHostFragment: BaseHostFragment?
        get() = currentFragment as? BaseHostFragment
}