package com.example.there.findclips.main.spotify

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.example.there.findclips.base.fragment.BaseNavHostFragment
import com.example.there.findclips.fragment.account.AccountNavHostFragment
import com.example.there.findclips.fragment.dashboard.DashboardNavHostFragment
import com.example.there.findclips.fragment.favourites.FavouritesHostFragment
import com.example.there.findclips.view.viewpager.adapter.CurrentFragmentStatePagerAdapter

class SpotifyMainPagerAdapter(manager: FragmentManager) : CurrentFragmentStatePagerAdapter(manager) {

    private val fragments: Array<Fragment> = arrayOf(DashboardNavHostFragment(), AccountNavHostFragment(), FavouritesHostFragment())

    val currentNavHostFragment: BaseNavHostFragment?
        get() = currentFragment as? BaseNavHostFragment

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size
}